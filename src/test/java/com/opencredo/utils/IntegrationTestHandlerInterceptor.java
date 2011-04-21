package com.opencredo.utils;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.portlet.HandlerInterceptor;
import org.springframework.web.portlet.ModelAndView;

public class IntegrationTestHandlerInterceptor implements HandlerInterceptor {

    private ModelMap model;
    private String viewName;
    private Exception exception = null;

    public ModelMap getModel() {
    	return model;
    }

    public String getViewName() {
    	return viewName;
    }

    public boolean didSomethingThrowAnException() {
    	if (exception != null) {
    		return true;
    	}
    	return false;
    }

    public boolean preHandleAction(ActionRequest request, ActionResponse response, Object handler) {
        return true;
    }

    public void afterActionCompletion(ActionRequest request, ActionResponse response, Object handler, Exception ex) {
    	exception = ex;
    }

    public boolean preHandleRender(RenderRequest request, RenderResponse response, Object handler) {
        return true;
    }

    public void postHandleRender(RenderRequest request, RenderResponse response, Object handler, ModelAndView modelAndView) {
    	model = modelAndView.getModelMap();
    	viewName = modelAndView.getViewName();
    }

    public void afterRenderCompletion(RenderRequest request, RenderResponse response, Object handler, Exception ex) {
    	exception = ex;
    }

    public boolean preHandleResource(ResourceRequest request, ResourceResponse response, Object handler) {
        return true;
    }

    public void postHandleResource(ResourceRequest request, ResourceResponse response, Object handler, ModelAndView modelAndView) {
    }

    public void afterResourceCompletion(ResourceRequest request, ResourceResponse response, Object handler, Exception ex) {
    	exception = ex;
    }

    public boolean preHandleEvent(EventRequest request, EventResponse response, Object handler) {
        return true;
    }

    public void afterEventCompletion(EventRequest request, EventResponse response, Object handler, Exception ex) {
    	exception = ex;
    }
}
