package com.sdyk.android.automator.test;

import com.sdyk.ormlite.WeixinFriend;
import org.junit.Test;

import java.util.Date;

public class ModelTest {

    @Test
    public void testAddWeixinFriend() {

        try {

            WeixinFriend wf = new WeixinFriend(
                    "123", "1", "", "2", "21");
            wf.insert();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        System.err.println((int) ((100D / 1000D) * 1600));
    }
}
