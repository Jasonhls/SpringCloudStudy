package com.cloud.clientserver.reactor;

/**
 * @Author: 何立森
 * @Date: 2023/07/31/17:02
 * @Description:
 */
public class ReactorDemo {
    public static void main(String[] args) throws InterruptedException {
        /**
         * Reactor有两种数据形式，分别用Flux和Mono表示，
         * 如果是Flux代表将来发送的是多个数据，
         * 如果是Mono代表将来放回的是1个数据(也有可能是0)
         */
        ReactorUserService userService = new ReactorUserService();
        ReactorFavoriteService favoriteService = new ReactorFavoriteService();
        userService.getFavorites(23L)
                //取前两个
                .take(2)
                //取详情
                .flatMap(id -> favoriteService.getDetail(id))
                .subscribe(value -> System.out.println(value + "---aaa--"), error -> System.out.println("process error"));

        while(true) {
            Thread.sleep(10000);
            System.out.println("-------做点其他事-----------");
        }
    }



}
