package com.racloop.accessibility.util;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class ContrastCheckerTest {

    @Test
    public void testPositive() {
        String hexColorForeground = "#0000FF";
        String hexColorBackground = "#FFFFFF";
        Color foreground = Color.decode(hexColorForeground);
        Color background = Color.decode(hexColorBackground);
        double contrastRatio = ContrastChecker.getContrastRatio(foreground, background);
        System.out.println("contrastRatio : " + contrastRatio);
        Assert.assertTrue(contrastRatio > 4.5);
    }

    @Test
    public void testNegative() {
        String hexColorForeground = "#404040";
        String hexColorBackground = "#A8A8A8";
        Color foreground = Color.decode(hexColorForeground);
        Color background = Color.decode(hexColorBackground);
        double contrastRatio = ContrastChecker.getContrastRatio(foreground, background);
        System.out.println("contrastRatio : " + contrastRatio);
        Assert.assertFalse(contrastRatio > 4.5);
    }
}
