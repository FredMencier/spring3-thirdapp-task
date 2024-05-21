package ch.heg.task.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebFilter
@Slf4j
public class ControllerFilter implements Filter {

     @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain filterChain) throws IOException, ServletException {
         long start = System.currentTimeMillis();
         String requestURI = ((RequestFacade) req).getRequestURI();
         log.info("Receive request for %s".formatted(requestURI));
         filterChain.doFilter(req, rep);
         long duration = System.currentTimeMillis() - start;
         log.info("Response in %s ms".formatted(duration));
    }
}
