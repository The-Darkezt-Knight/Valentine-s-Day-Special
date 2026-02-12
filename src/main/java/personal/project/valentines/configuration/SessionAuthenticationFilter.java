package personal.project.valentines.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that protects the main-panel view from direct URL access.
 * Only users who have successfully passed authentication (session flag)
 * are allowed to view the letters page.
 */
@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private static final String PROTECTED_PATH = "/view/main-panel.html";
    private static final String SESSION_AUTH_KEY = "authenticated";
    private static final String REDIRECT_PATH = "/view/onboarding-page.html";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (PROTECTED_PATH.equals(path)) {
            HttpSession session = request.getSession(false);

            boolean isAuthenticated = session != null
                    && Boolean.TRUE.equals(session.getAttribute(SESSION_AUTH_KEY));

            if (!isAuthenticated) {
                response.sendRedirect(REDIRECT_PATH);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
