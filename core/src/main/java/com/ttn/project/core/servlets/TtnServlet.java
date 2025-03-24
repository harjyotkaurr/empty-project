package com.ttn.project.core.servlets;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONObject;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

import org.osgi.service.component.annotations.Component;


@Component(service = Servlet.class)
@SlingServletPaths(value={"/bin/ttnproject"})
public class TtnServlet extends SlingSafeMethodsServlet {




    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        String pagePath = request.getParameter("pagePath");
        String newTitle = request.getParameter("newTitle");

        if (pagePath == null || newTitle == null) {

            response.getWriter().write("not found");
            return;
        }

        ResourceResolver resourceResolver = request.getResourceResolver();

        Resource pageResource = resourceResolver.getResource(pagePath);

        if (pageResource == null) {

            response.getWriter().write("Page not found.");
            return;
        }

        String previousTitle = pageResource.adaptTo(Page.class).getTitle();

        pageResource.adaptTo(ModifiableValueMap.class).put("jcr:title", newTitle);

        resourceResolver.commit();

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("previousTitle", previousTitle);
        jsonResponse.put("newTitle", newTitle);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }
}























//http://localhost:4502/bin/updateTitle?pagePath=/content/mysite/en/my-page&newTitle=New+Page+Title

