/*
 * Copyright (C) 2005 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License  as
 * published by the Free Software Foundation, either version 3 of  the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero  General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public  License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.jasperserver.api;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JSException.java 19921 2010-12-11 14:52:49Z tmatyashovsky $
 */
public class JSException extends RuntimeException {

	private static final long serialVersionUID = -323095206874495879L;
	
	protected Object[] args;
	private boolean wrapperObject;
	
	public JSException(String message) {
		super(message);
	}

	public JSException(String message, Throwable cause) {
		super(message, cause);
		this.wrapperObject = true;
	}

	public JSException(Throwable cause) {
		super(cause);
		this.wrapperObject = true;
	}

	public JSException(String message, Object[] args) {
		super(message);
		this.args = args;
	}

	/**
	 * @return Returns the args.
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * @param args The args to set.
	 */
	public void setArgs(Object[] args) {
		this.args = args;
	}

	/**
	 * @return Returns the wrapperObject.
	 */
	public boolean isWrapperObject() {
		return wrapperObject;
	}

	/**
	 * @param wrapperObject The wrapperObject to set.
	 */
	public void setWrapperObject(boolean wrapperObject) {
		this.wrapperObject = wrapperObject;
	}
}
