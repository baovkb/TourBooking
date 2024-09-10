package com.vkbao.travelbooking;

import org.junit.Test;

import static org.junit.Assert.*;

import com.vkbao.travelbooking.Models.Invoice;
import com.vkbao.travelbooking.Models.Order;
import com.vkbao.travelbooking.Repositories.OrderRepository;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    OrderRepository orderRepository = new OrderRepository();
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}