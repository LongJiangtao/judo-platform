package com.github.judo.gateway.component.filter;

import com.github.judo.gateway.service.LogSendService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

/**
 * @Auther: xiangjunzhong@qq.com
 * @Description: 网关统一异常处理
 * @Version: 1.0
 */
@Component
public class ErrorHandlerFilter extends ZuulFilter {
    @Autowired
    private LogSendService logSendService;

    @Override
    public String filterType() {
        return ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        return requestContext.getThrowable() != null;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        logSendService.send(requestContext);
        return null;
    }
}

