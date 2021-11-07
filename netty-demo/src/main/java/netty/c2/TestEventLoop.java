package netty.c2;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @title: TestEventLoop
 * @Description // EventLoop是一个单线程执行器（同时维护了一个selector），里面的run方法处理channel上的 IO 事件
 *              // 继承至JUC 线程池 与 netty 自己的 OrderedEventExecutor
 * @Author Chen
 * @Date: 2021/11/7 12:30
 * @Version 1.0
 */
@Slf4j
public class TestEventLoop {

    public static void main(String[] args) {

        // 1. 创建事件循环组
        EventLoopGroup group = new NioEventLoopGroup(2);  // 可以处理 IO事件，普通任务，定时任务
//        System.out.println(NettyRuntime.availableProcessors());  // 打印CPU的核数

        // 2. 获取下一个事件循环对象
        /*System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());*/

        // 3. 执行普通任务
        /*group.submit(()->{
            log.debug("ok");
        });
        log.debug("main");*/

        // 4. 执行定时任务
        group.scheduleAtFixedRate(()->{
            log.debug("ok");
        },0, 1, TimeUnit.SECONDS);

        group.scheduleAtFixedRate(()->{
            log.debug("ok-2");
        },0, 2, TimeUnit.SECONDS);

    }

}
