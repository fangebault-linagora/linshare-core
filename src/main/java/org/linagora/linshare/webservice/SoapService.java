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
 *   License along with Linshare.  If not, see
 *                                    <http://www.gnu.org/licenses/>.
 *
 *   (c) 2008 Groupe Linagora - http://linagora.org
 *
*/
package org.linagora.linshare.webservice;

import java.util.List;

import javax.jws.WebService;

import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.webservice.dto.Document;
import org.linagora.linshare.webservice.dto.DocumentAttachement;
import org.linagora.linshare.webservice.dto.SimpleLongValue;


/**
 * Soap interface
 */



@WebService
public interface SoapService {

	// Documents
	public List<Document> getDocuments() throws BusinessException;
	public SimpleLongValue getUserMaxFileSize() throws BusinessException;
	public SimpleLongValue getAvailableSize() throws BusinessException;
	public Document addDocumentXop(DocumentAttachement doca) throws BusinessException;
	
	// Shares
	public void sharedocument(String targetMail, String uuid, int securedShare) throws BusinessException;
	
	// PluginManagment
	public String getInformation() throws BusinessException;
}