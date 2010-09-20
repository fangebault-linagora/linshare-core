/*
 *    This file is part of Linshare.
 *
 *   Linshare is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as
 *   published by the Free Software Foundation, either version 3 of
 *   the License, or (at your option) any later version.
 *
 *   Linshare is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public
 *   License along with Foobar.  If not, see
 *                                    <http://www.gnu.org/licenses/>.
 *
 *   (c) 2008 Groupe Linagora - http://linagora.org
 *
*/
package org.linagora.linShare.view.tapestry.components;


import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;

/**
 * Popup that is openable wait for a password, and if it is not suitable, shake the window
 * This is not conveniently integrated for the moment
 * 
 * The right way to use it : 
 *  * in the opener page, have something like "window1.showCenter(true)" (window1 is the window component name here)
 *  * t:title and t:content t:errorBanner are expected to customize the window
 *  * all the mechanics about form submit isn't done here. the opener should validate the form, and
 *    regarding the result, either return formFail or formSuccess
 *    
 * Probably we should use another parameter to put the link to open the window, and have a more suitable component
 * @author ncharles
 *
 */
public class PasswordPopupSubmit {

	/* ***********************************************************
     *                         Parameters
     ************************************************************ */
	// Title of the window
	@Parameter("title")
	private String title;
	
	// Content of the window
	@SuppressWarnings("unused")
	@Parameter("content")
	@Property
	private String content;
	
	@Parameter(required=true,defaultPrefix=BindingConstants.LITERAL)
	@Property
	private String formId;
	
	
	// error banner
	@Parameter("errorBanner")
	@Property
	private String errorBanner;
    /* ***********************************************************
     *                      Injected services
     ************************************************************ */

	/* ***********************************************************
     *                Properties & injected symbol, ASO, etc
     ************************************************************ */
	
    @Component(parameters = {"style=bluelighting", "show=false", "title=title", "width=450", "height=100"})
    private WindowWithEffects window_passwordPopupSubmit;
    
    
	// The form that holds the password request
	@InjectComponent
	private Form formPassword;
	
	// The zone that contains the action to be thrown on success 
	@InjectComponent
	private Zone onSuccess;
	
	// The form zone
	@InjectComponent
	private Zone formZone;
	
    private String password;
    
    /* ***********************************************************
     *                   Event handlers&processing
     ************************************************************ */
    @SetupRender
    public void cleanError() {
    	formPassword.clearErrors();
    }
    

	// When the form has a failure, we throw this
	public Zone formFail() {
		return formZone;
	}
	
	// When the form has a success, we throw this
	public Zone formSuccess() {
		return onSuccess;
	}
	
    /* ***********************************************************
     *                      Getters & Setters
     ************************************************************ */ 
	
	
	public Form getFormPassword() {
		return formPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getJSONId() {
		return window_passwordPopupSubmit.getJSONId();
	}
	
	public String getTitle() {
		return title;
	}
}