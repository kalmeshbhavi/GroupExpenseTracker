/**
 * 
 */
package com.group.expense.controller;

import com.group.expense.dao.UserDao;
import com.group.expense.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author AKivanda
 *
 */
@Component
@Path("/user/")
public class SignUpController {

    @Autowired
    private UserDao userDao;

    @POST
    @Path("signupuser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUpUser(User user) {
        User existingUser = userDao.getUserByUserName(user.getUserName());
        if (existingUser != null) {
            return Response.status(Response.Status.CONFLICT).entity("User with this username already exists").build();
        }
        else {
            userDao.addUser(user);
        }
        return Response.status(200).entity(true).build();
    }

    @POST
    @Path("signin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signIn(User user) {
        User existingUser = userDao.getUserByUserName(user.getUserName());
        if (existingUser != null) {
            if (user.getPassword().equals(existingUser.getPassword())) {
                return Response.status(200).entity(true).build();
            }
            else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Username or password does not match")
                        .build();
            }
        }
        else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Username or password does not match").build();
        }
    }

    @GET
    @Path("allusersofgroups/{groupName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsersOfGroup(@PathParam("groupName") String groupName) {
        return userDao.getAllUsers(groupName);
    }

    @DELETE
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(User user) {
        userDao.deleteUser(user);
        return Response.status(200).entity(true).build();
    }
}
