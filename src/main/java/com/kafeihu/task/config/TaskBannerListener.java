package com.kafeihu.task.config;

import com.kafeihu.task.core.constant.Version;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * date: 2022/10/13 17:40
 * 增加Task框架 banner
 *
 * @author kroulzhang
 */
public class TaskBannerListener implements ApplicationListener<ApplicationContextInitializedEvent> {

    private static final AtomicBoolean PROCESSED = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        if (PROCESSED.compareAndSet(false, true)) {
            String bannerText = buildBannerText();
            System.out.print(bannerText);
        }
    }

    private String buildBannerText() {
        return "\n _________      _        ______    ___  ____   \n"
                + "|  _z_k_  |    / \\     .' ____ \\  |_  ||_  _|  \n"
                + "|_/ | | \\_|   / _ \\    | (___ \\_|   | |_/ /    \n"
                + "    | |      / ___ \\    _.____`.    |  __'.    \n"
                + "   _| |_   _/ /   \\ \\_ | \\____) |  _| |  \\ \\_  \n"
                + "  |_____| |____| |____| \\______.' |____||____| \n"
                + " :: TASK ::   (" + Version.version() + ") \n"
                + "                                       \n";
    }
}
