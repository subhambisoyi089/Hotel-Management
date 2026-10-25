package com.hotel.web;

import com.hotel.config.HotelProperties;
import com.hotel.entity.RoomType;
import com.hotel.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;

/** Values every page needs, plus friendly handling of errors the user can fix. */
@ControllerAdvice
public class GlobalControllerAdvice {

    private final HotelProperties hotel;

    public GlobalControllerAdvice(HotelProperties hotel) {
        this.hotel = hotel;
    }

    @ModelAttribute("hotel")
    public HotelProperties hotel() {
        return hotel;
    }

    /** Today as yyyy-MM-dd, for the min attribute of date inputs. */
    @ModelAttribute("today")
    public String today() {
        return LocalDate.now().toString();
    }

    @ModelAttribute("todayText")
    public String todayText() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy", Locale.ENGLISH));
    }

    @ModelAttribute("roomTypes")
    public RoomType[] roomTypes() {
        return RoomType.values();
    }

    @ExceptionHandler(BusinessException.class)
    public String handleBusiness(BusinessException ex, HttpServletRequest request) {
        return backWithError(request, ex.getMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public String handleBadInput(HttpServletRequest request) {
        return backWithError(request, "Some values are missing or invalid. Check the form and try again.");
    }

    /** Flash the message and go back to the page the user came from. */
    private String backWithError(HttpServletRequest request, String message) {
        FlashMap flash = RequestContextUtils.getOutputFlashMap(request);
        if (flash != null) {
            flash.put("error", message);
        }
        return "redirect:" + previousPath(request);
    }

    private String previousPath(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null) {
            try {
                URI uri = URI.create(referer);
                String path = uri.getRawPath();
                String context = request.getContextPath();
                if (path != null && !context.isEmpty() && path.startsWith(context)) {
                    path = path.substring(context.length());
                }
                // Only same-site paths; "//host" would be an open redirect.
                if (path != null && path.startsWith("/") && !path.startsWith("//")) {
                    return uri.getRawQuery() == null ? path : path + "?" + uri.getRawQuery();
                }
            } catch (IllegalArgumentException ignored) {
                // bad Referer header, use the home page
            }
        }
        return "/";
    }
}
