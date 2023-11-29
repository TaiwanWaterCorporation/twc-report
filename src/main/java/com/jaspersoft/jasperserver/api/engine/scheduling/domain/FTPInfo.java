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
package com.jaspersoft.jasperserver.api.engine.scheduling.domain;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holder for FTP information
 * 
 * @author Ivan Chan (ichan@jaspersoft.com)
 * @version $Id: FTPInfo.java 22627 2012-03-19 19:41:28Z ichan $
 * @since 1.0
 */
@XmlRootElement
public class FTPInfo implements Serializable {

	private static final long serialVersionUID = 1L;
    String userName;
    String password;
    String folderPath;
    String serverName;
    Map<String, String> propertiesMap;

	/**
	 * Creates an empty FTP information holder.
	 */
	public FTPInfo() {
	}

	/**
	 * Returns the login user name for the FTP server
	 *
	 * @return the login user name
	 */
    public String getUserName() {
        return userName;
    }

    /**
	 * Specifies whether the login user name for the FTP server
	 *
	 * @param  userName the login user name
	 */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
	 * Returns the login password for the FTP server
	 *
	 * @return the login password
	 */
    public String getPassword() {
        return password;
    }

     /**
	 * Specifies whether the login password for the FTP server
	 *
	 * @param password the login password
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * Returns the path of the folder under which job output
	 * resources would be created.
	 *
	 * @return the folder path
	 */
    public String getFolderPath() {
        return folderPath;
    }

    /**
	 * Specifies whether the path of the folder under which job output
	 * resources would be created.
	 *
	 * @param folderPath the folder path
	 */
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    /**
	 * Returns the server name for the ftp site.
	 *
	 * @return the server name
	 */
    public String getServerName() {
        return serverName;
    }

    /**
	 * Specifies whether the server name for the ftp site.
	 *
	 * @param serverName the server name
	 */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Returns the additional properties for ftp info
     *
     * @return the additional properties for FTP info
     */
    public Map<String, String>  getPropertiesMap() {
        return propertiesMap;
    }

    /**
     * Sets the set additional properties for FTP info
     *
     * @param propertiesMap extra properties for FTP info
     */
    public void setPropertiesMap(Map<String, String>  propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

}
