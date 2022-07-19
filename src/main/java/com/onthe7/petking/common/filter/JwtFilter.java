package com.onthe7.petking.common.filter;

import com.onthe7.petking.common.security.domain.JwtUserDetails;
import com.onthe7.petking.common.security.service.SecurityUserService;
import com.onthe7.petking.common.security.tokens.JwtPreAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer (.+?)$");
  private final SecurityUserService userService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
  ) throws ServletException, IOException {
      String token = getToken(request);

      if(StringUtils.isEmpty(token)) {
          filterChain.doFilter(request, response);
          return;
      }

      JwtUserDetails userDetails = userService.loadUserByToken(token);
      JwtPreAuthenticationToken jwtPreAuthenticationToken =
              JwtPreAuthenticationToken.builder()
                      .principal(userDetails)
                      .details(new WebAuthenticationDetailsSource().buildDetails(request)).build();

      SecurityContextHolder.getContext().setAuthentication(jwtPreAuthenticationToken);

      filterChain.doFilter(request, response);
  }

  private String getToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER)).filter(not(String::isEmpty))
        .map(BEARER_PATTERN::matcher).filter(Matcher::find).map(matcher -> matcher.group(1)).orElse(null);
  }
}
