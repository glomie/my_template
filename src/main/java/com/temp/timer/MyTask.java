package com.temp.timer;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author wujunyan
 * Created on 2025-10-31
 */
public class MyTask extends TimerTask {

    @Override
    public void run() {
        System.out.println("任务执行了，时间为：" + new Date());
    }
}
