package com.xanadutec.coreflex.propertyEditors;

import java.beans.PropertyEditorSupport;

import com.xanadutec.coreflex.model.RoleModel;
import com.xanadutec.coreflex.userTask.UserService;


 public class UserRolePropertyEditors extends PropertyEditorSupport{

	private UserService userService;

    public UserRolePropertyEditors(UserService userService){
        this.userService = userService;
    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        RoleModel roleModel = null;
        try {
            Integer id = Integer.parseInt(text);
            roleModel = userService.userRoll(id);
            
        } catch (NumberFormatException ex) {
            System.out.println("Department will be null");
        }
        setValue(roleModel);
    }
}
