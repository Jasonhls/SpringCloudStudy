package com.cloud.testclient.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/hystrixCommand")
public class HystrixCommandController {
    /**
     * 熔断触发降级
     * 熔断器开启的两个条件：
     *  请求数达到设定的阀值
     *  请求的失败数 / 总请求数 > 错误占比阀值
     */
    @GetMapping(value = "/circuitBreaker/{num}")
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),  //开启熔断降级功能
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"), //最小请求次数，这里是5个请求
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"), //熔断时间为5秒
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50") //错误比例超过50%，就熔断
    }, fallbackMethod = "circuitBreakerFallback") //触发回调方法，当前类下的方法名，请求服务方法和回调方法，入参要一致，不然会报错的
    public String circuitBreaker(@PathVariable(value = "num") int num) {
        if(num % 2 == 0) {
            return "正常访问";
        }
        throw new RuntimeException("");
    }

    public String circuitBreakerFallback(int num) {
        return "熔断-触发降级";
    }

    /**
     * 超时触发降级
     */
    @GetMapping("/timeOut")
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.timeout.enabled", value = "true"), //启动超时触发降级
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000") //超过一秒就触发降级
    }, fallbackMethod = "timeOutFallback")
    public String timeOut() throws InterruptedException {
        Thread.sleep(3000);
        return "正常访问";
    }

    public String timeOutFallback() {
        return "超时-触发降级";
    }

    /**
     * 资源隔离触发降级
     * 资源隔离触发降级又分成两种：
     *      信号量隔离：是通过限制依赖服务的并发请求数，来控制隔离开关，它不会使用Hystrix管理的线程池处理请求。使用容器(Tomcat)的线程处理请求逻辑。
     *          特点：不涉及线程切换，资源调度，上下文的切换等，相对效率高。触发条件：信号量隔离也会启动熔断机制。如果请求并发数超标，则触发熔断，返回fallback数据。
     *      线程池隔离：hystrix进行资源隔离，其实是提供了一个抽象，叫做command，就是说，如果要把对某一个依赖服务的所有调用请求，全部隔离在同一份资源池内，对这个依赖服务的所有调用请求，
     *          全部走这个资源池内的资源，不会去用其他的资源了，这个就叫做资源隔离。
     *          比如：对某一个商品服务所有的调用请求，全部隔离到一个线程池内，对商品服务的每次调用请求都封装在一个command里面，每个command（每次服务调用请求）都是使用线程池内的一个线程去执行的，
     *          所以哪怕现在这个商品服务同时发起的调用量已经到了1000了，但是线程池内就10个线程，其他的请求都会存到等待队列中，最多就只会用这10个线程去执行请求，对商品服务的请求，因为接口调用延迟。
     *          不会将tomcat内部所有的线程全部耗尽。
     *      使用线程隔离的好处：
     *          应用程序可以不受失控的第三方客户端的威胁，如果第三方客户端出现问题，可以通过降级来隔离依赖。
     *          当失败的客户端服务恢复时，线程池将会被清除，应用程序也会恢复，而不至于使整个Tomcat容器出现故障。
     *          简而言之，由线程提供的隔离功能可以使客户端和应用程序优雅的处理各种变化，而不会造成中断。
     *      线程池的缺点：
     *          线程最主要的缺点就是增加了CPU的计算开销，每个command都会在单独的线程上执行，这样的执行方式会涉及到命令的排队、调度和上下文切换。
     *          Netflix在涉及这个系统时，决定接受这个开销的代价，来换取它所提供的好处，并且认为这个开销是足够小的，不会有重大的成本或者性能影响。
     *
     */

    /**
     * 信号量隔离
     */
    @GetMapping(value = "/semaphore")
    @HystrixCommand(
            commandProperties = {
                    //默认是THREAD线程池隔离。设置信号量隔离后，线程池相关配置失效。
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"), //隔离的种类，可选值只有THREAD（线程池隔离）和SEMAPHORE（信号量隔离）。
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "2") //信号量大小，这服务只能两个请求同时访问，信号量最大并发数。默认是10，常见配置500-10000。
            },
            fallbackMethod = "semaphoreFallback"
    )
    public String semaphore() {
        return "semaphore正常访问";
    }

    public String semaphoreFallback() {
        return "semaphore-触发降级";
    }


    private int num1 = 1;

    /**
     * 线程池隔离
     */
    @GetMapping(value = "/thread")
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"), //使用线程池隔离
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"), //超时设置为3秒，默认是1s
    },
    threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "20"), //线程池大小，默认是5个
            @HystrixProperty(name = "maxQueueSize", value = "1000"), //等待队列长度，注意，该设置只会在初始化时有效，之后不能修改threadPool的queue size
            @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"), //线程存活时间，如果corePoolSize和maxPoolSize设置一样（默认实现）该设置无效
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "800"), //即使maxQueueSize没有达到，达到queueSizeRejectionThreshold该值后，请求也会被拒绝。
            //因为maxQueueSize不能被动态修改，这个参数将允许我们动态设置该值。if maxQueueSize == -1，该字段将不起作用
    },
            //command group一般来说，可以是对应一个服务，多个command key对应这个服务的多个接口，多个接口的调用共享同一个线程池，如果说你的command key，要用自己的线程池，可以定义自己的threadPoolKey
            groupKey = "ThreadService", //群组的key
            commandKey = "thread",  //代表了一类command，一般来说，代表了底层的依赖服务的一个接口
            threadPoolKey = "ThreadService", //线程池的名字，默认的threadPoolKey就是command group名称
            fallbackMethod = "threadFallback"
    )
    public void thread() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(Thread.currentThread() + "正常访问" + num1++);
    }

    public void threadFallback() {
        System.out.println("线程池-触发熔断" + new Date());
    }

}
