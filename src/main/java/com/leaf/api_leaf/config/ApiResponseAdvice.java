package com.leaf.api_leaf.config;

import com.leaf.api_leaf.dto.response.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // ✅ Excluye String, byte[], y rutas de Swagger para no romperlas
        String path = returnType.getDeclaringClass().getPackageName();
        return !returnType.getParameterType().equals(String.class)
                && !returnType.getParameterType().equals(byte[].class)
                && !path.contains("springdoc")
                && !path.contains("springfox");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {


        if (body instanceof ApiResponse) {
            return body;
        }


        if (body == null) {
            return ApiResponse.ok(null);
        }


        String uri = request.getURI().getPath();
        if (uri.contains("/swagger-ui")
                || uri.contains("/v3/api-docs")
                || uri.contains("/actuator")) {
            return body;
        }

        return ApiResponse.ok(body);
    }
}