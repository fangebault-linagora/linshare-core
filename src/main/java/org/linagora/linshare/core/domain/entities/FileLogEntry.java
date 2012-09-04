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
package org.linagora.linshare.core.domain.entities;

import org.linagora.linshare.core.domain.constants.LogAction;

/**
 * Log class for uploading/deleting/expiring files
 * @author ncharles
 *
 */
public class FileLogEntry extends LogEntry {

	
	private static final long serialVersionUID = -7747367540741943254L;

	protected String fileName;
	
	protected Long fileSize;
	
	protected String fileType;


	protected FileLogEntry() {
		super();
		this.fileName = null;
		this.fileSize = null;
		this.fileType = null;
	}
	
	
	public FileLogEntry(Account actor, LogAction logAction, String description, String fileName, Long fileSize, String fileType) {
		super(actor, logAction, description);
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.fileType = fileType;
	}
	
	
	public String getFileName() {
		return fileName;
	}
	

	public Long getFileSize() {
		return fileSize;
	}

	
	public String getFileType() {
		return fileType;
	}
	
}
