package com.identa.catering.util;

import com.google.gson.Gson;
import com.identa.catering.model.dto.OrderDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.WebUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Util {

    public final static String REDIRECT_TO_PRODUCTS = "redirect:/main/products";

    public static OrderDTO getOrderDTOFromCookie(HttpServletRequest request) {
        Gson gson = new Gson();
        Cookie cookie = WebUtils.getCookie(request, "order");
        if (cookie != null && cookie.getValue() != null)
            return gson.fromJson(URLDecoder.decode(cookie.getValue(), UTF_8), OrderDTO.class);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderItems(new ArrayList<>());
        return orderDTO;
    }

    public static void saveOrderDTOAsCookie(HttpServletResponse response, OrderDTO orderDTO) {
        Gson gson = new Gson();
        Cookie cookie = new Cookie("order",
                URLEncoder.encode(gson.toJson(orderDTO),
                        UTF_8));
        cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static OrderDTO initOrder(HttpServletResponse response, OrderDTO orderDTO) {
        orderDTO = new OrderDTO();
        orderDTO.setOrderItems(new ArrayList<>());
        saveOrderDTOAsCookie(response, orderDTO);
        return orderDTO;
    }



}
