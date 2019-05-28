package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//To work request needs header 'Authorization: Bearer jwtTokenHere'

@WebServlet("/servlet")
@ServletSecurity(@HttpConstraint(rolesAllowed = "admin"))
public class SecuredServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/plain");
    	PrintWriter out = response.getWriter();
    	
        String webName = null;
        if (request.getUserPrincipal() != null) {
            webName = request.getUserPrincipal().getName();
        }
        
        out.print(
                "This is a protected servlet \n" +
        
                    "web username: " + webName + "\n" +
                            
                    "web user has role \"admin\": " + request.isUserInRole("admin") + "\n" +
                    "web user has role \"boss\": " + request.isUserInRole("boss") + "\n"
                    );
        
        response.flushBuffer();
    }

}
