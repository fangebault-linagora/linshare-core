/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2014 LINAGORA
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version, provided you comply with the Additional Terms applicable for
 * LinShare software by Linagora pursuant to Section 7 of the GNU Affero General
 * Public License, subsections (b), (c), and (e), pursuant to which you must
 * notably (i) retain the display of the “LinShare™” trademark/logo at the top
 * of the interface window, the display of the “You are using the Open Source
 * and free version of LinShare™, powered by Linagora © 2009–2014. Contribute to
 * Linshare R&D by subscribing to an Enterprise offer!” infobox and in the
 * e-mails sent with the Program, (ii) retain all hypertext links between
 * LinShare and linshare.org, between linagora.com and Linagora, and (iii)
 * refrain from infringing Linagora intellectual property rights over its
 * trademarks and commercial brands. Other Additional Terms apply, see
 * <http://www.linagora.com/licenses/> for more details.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License and
 * its applicable Additional Terms for LinShare along with this program. If not,
 * see <http://www.gnu.org/licenses/> for the GNU Affero General Public License
 * version 3 and <http://www.linagora.com/licenses/> for the Additional Terms
 * applicable to LinShare software.
 */
package org.linagora.linshare.view.tapestry.components;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.Response;
import org.linagora.LinThumbnail.utils.Constants;
import org.linagora.linshare.core.domain.vo.DocToSignContext;
import org.linagora.linshare.core.domain.vo.DocumentVo;
import org.linagora.linshare.core.domain.vo.ShareDocumentVo;
import org.linagora.linshare.core.domain.vo.UserVo;
import org.linagora.linshare.core.exception.BusinessErrorCode;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.exception.TechnicalErrorCode;
import org.linagora.linshare.core.exception.TechnicalException;
import org.linagora.linshare.core.facade.AbstractDomainFacade;
import org.linagora.linshare.core.facade.DocumentFacade;
import org.linagora.linshare.core.facade.ShareFacade;
import org.linagora.linshare.core.utils.FileUtils;
import org.linagora.linshare.view.tapestry.enums.ActionFromBarDocument;
import org.linagora.linshare.view.tapestry.enums.BusinessUserMessageType;
import org.linagora.linshare.view.tapestry.models.SorterModel;
import org.linagora.linshare.view.tapestry.models.impl.SharedFileSorterModel;
import org.linagora.linshare.view.tapestry.objects.BusinessUserMessage;
import org.linagora.linshare.view.tapestry.objects.FileStreamResponse;
import org.linagora.linshare.view.tapestry.objects.MessageSeverity;
import org.linagora.linshare.view.tapestry.services.BusinessMessagesManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SupportsInformalParameters
@Import(library = { "ListDocument.js"})
public class ListSharedDocument {

	private static final Logger logger = LoggerFactory.getLogger(ListSharedDocument.class);
	
	/***********************************
	 * Parameters
	 ***********************************/
	/**
	 * The user owner for the document list.
	 */
	@Parameter(required=true,defaultPrefix=BindingConstants.PROP)
	private UserVo user;

	/**
	 * The list of documents.
	 */
	@Parameter(required=true,defaultPrefix=BindingConstants.PROP)
	@Property
	private List<ShareDocumentVo> shareDocuments;
    
    @Parameter(required = false, defaultPrefix = BindingConstants.PROP)
    @Property
    private boolean inSearch;
    
    @Parameter(required = false, defaultPrefix = BindingConstants.PROP)
    @Property
    private boolean disableDeletion;
	
	
	/***********************************
	 * Properties
	 ***********************************/
	

	@Property
	private ShareDocumentVo shareDocument;

	@Property
	@Persist
	private List<ShareDocumentVo> listSelected;
	

	/***********************************
	 * Service injection
	 ***********************************/
	
	@Inject
	private PersistentLocale persistentLocale;
	
	@Inject
	private DocumentFacade documentFacade;

	@Inject
	private ShareFacade shareFacade;

	@Inject
	private AbstractDomainFacade domainFacade;
	
	@Inject
	private ComponentResources componentResources; 
	
	@Inject
	private Response response;

	@Inject
	private BeanModelSource beanModelSource;
	
	@InjectComponent
	private UserDetailsDisplayer userDetailsDisplayer;
	
	@Component(parameters={ "share=true" })
	private SignatureDetailsDisplayer signatureDetailsDisplayer;
	
	@InjectComponent
	private PasswordDecryptPopup passwordDecryptPopup;
	
	@Inject
	private PageRenderLinkSource linkFactory;
	
	
	@Property
	@Persist
	private BeanModel model;
	
	@Inject
	private Messages messages;

    @Inject
    private BusinessMessagesManagementService businessMessagesManagementService;
	
	/***********************************
	 * Flags
	 ***********************************/
	@Persist
	private String currentUuid;
	
	@Persist
	private List<ShareDocumentVo> componentdocuments;
	
	@Property
	private boolean activeSignature;
	
	@Property
	private boolean activeEncypher;
	
	@Property
	private boolean enabledToUpload;
	
	@Persist
	private boolean refreshFlag;
	
	@Persist
	private List<ShareDocumentVo> docs;


	@SuppressWarnings("unused")
	private boolean filesSelected;

	@Property
	private Boolean valueCheck;

	@Persist("flash")
	private ActionFromBarDocument actionbutton;
	
	@Property
	private String action;

	@Property
	private String deleteConfirmed;

	@Persist
	private String pass;
	
	
	/**
	 * Components Model.
	 */
	@Property
	@Persist
	private SorterModel<ShareDocumentVo> sorterModel;
	

	@Property
	@InjectComponent
	private ShareEditForm shareEditForm;
	
	
	
	/*********************************
	 * Phase render
	 *********************************/

	/**
	 * Initialization of the selected list and set the userLogin from the user ASO.
	 * @throws BusinessException 
	 */
	@SetupRender
	public void init() throws BusinessException {
		listSelected = new ArrayList<ShareDocumentVo>();
		Collections.sort(shareDocuments);
		this.componentdocuments = shareDocuments;
		this.activeSignature = documentFacade.isSignatureActive(user);
		this.activeEncypher = documentFacade.isEnciphermentActive(user);
		//if(model==null) // need to redo the model each type, for the config may change 
		model=initModel();
		
	}

	/********************************
	 * ActionLink methods
	 *********************************/
	
	/**
	 * The action declenched when the user click on the download link on the name of the file. 
	 */
	public StreamResponse onActionFromDownload(String uuid) throws BusinessException{
		logger.debug("onActionFromDownload");
		// when user has been logged out
		if (componentdocuments == null) {
			return null;
		}

		ShareDocumentVo currentSharedDocumentVo=searchDocumentVoByUUid(componentdocuments, uuid);
		
		if (null == currentSharedDocumentVo) {
			businessMessagesManagementService.notify(new BusinessException(
					BusinessErrorCode.SHARED_DOCUMENT_NOT_FOUND,
					"invalid uuid for this user"));
			return null;
		} else {
			boolean alreadyDownloaded = currentSharedDocumentVo.getDownloaded();
			InputStream stream = shareFacade.getShareStream(user, currentSharedDocumentVo.getIdentifier());
			
			// send an email to the owner if it is the first time the document is downloaded
			if (!alreadyDownloaded) {
				logger.info("First download of this shar, notify the owner of it.");
				notifyOwnerByEmail(currentSharedDocumentVo);
				componentdocuments = shareFacade.getAllSharingReceivedByUser(user); // maj valeur downloaded dans le VO
			}
			return new FileStreamResponse(currentSharedDocumentVo, stream);
		}

	}
	
	private void notifyOwnerByEmail(ShareDocumentVo currentSharedDocumentVo) {
		try {
			shareFacade.sendDownloadNotification(currentSharedDocumentVo, user);
		} catch (BusinessException e) {
			logger.error("Problem while sending mail", e);
			throw new TechnicalException(TechnicalErrorCode.MAIL_EXCEPTION,"Problem while sending mail",e);
		}		
	}

	public void onActionFromDelete(String uuid){
		currentUuid = uuid;
	}
	
	public Object onActionFromSignature(String uuid) throws BusinessException{
		currentUuid = uuid;
		ShareDocumentVo shareddoc = searchDocumentVoByUUid(componentdocuments,uuid);
		
		if (null==shareddoc) {
			throw new BusinessException(BusinessErrorCode.INVALID_UUID,"invalid uuid for this user");
		} else {
			// context is shared document
			return linkFactory.createPageRenderLinkWithContext("signature/SelectPolicy",
					new Object[]{DocToSignContext.SHARED.toString(), shareddoc.getIdentifier()});
		}
	}

	public void onActionFromDecryptIcon(String uuid)
			throws BusinessException {
		currentUuid = uuid;
		actionbutton = ActionFromBarDocument.DECRYPT_ACTION;
		passwordDecryptPopup.getFormPassword().clearErrors(); // delete popup message
	}
	
	public Zone onActionFromShowUser(String mail) throws BusinessException {
		return userDetailsDisplayer.getShowUser(mail);	
	}
	
	public Zone onActionFromShowSignature(String docidentifier) throws BusinessException {
		return signatureDetailsDisplayer.getShowSignature(docidentifier);
	}
	
    public void onActionFromCopy(String docIdentifier) {
    	logger.debug("onActionFromCopy");
        ShareDocumentVo shareDocumentVo = searchDocumentVoByUUid(componentdocuments, docIdentifier);
        boolean copyDone = false;
        
        //create the copy of the document and remove it from the received documents
        try {
            shareFacade.createLocalCopy(shareDocumentVo, user);
            copyDone = true;
        } catch (BusinessException e) {
            // process business exception. Can be thrown if no space left or wrong mime type.
            businessMessagesManagementService.notify(e);
        }
        
        if (copyDone) {
            businessMessagesManagementService.notify(new BusinessUserMessage(
            		BusinessUserMessageType.LOCAL_COPY_OK, MessageSeverity.INFO));
            componentResources.triggerEvent("resetListFiles", null, null);
        }
    }
    
	public Object onSuccessFromSearch() {
		if (listSelected.size() < 1) {
            businessMessagesManagementService.notify(new BusinessUserMessage(
            		BusinessUserMessageType.NOFILE_SELECTED, MessageSeverity.WARNING));
    		return null;
		}
		
		actionbutton = ActionFromBarDocument.fromString(action);
		
		switch (actionbutton) {
		case DELETE_ACTION:
			if ("true".equals(deleteConfirmed)) {
				componentResources.getContainer().getComponentResources()
						.triggerEvent("eventDeleteFromListDocument",
								listSelected.toArray(), null);
			}
			break;
		case SIGNATURE_ACTION:
			componentResources.getContainer().getComponentResources()
					.triggerEvent("eventSignatureFromListDocument",
							listSelected.toArray(), null);
			break;
		case COPY_ACTION:
			copyFromListDocument(listSelected);
			break;
		case FORWARD_ACTION:
			componentResources.getContainer().getComponentResources()
					.triggerEvent("eventForwardFromListDocument",
							listSelected.toArray(), null);
			break;
		case DECRYPT_ACTION:
			List<Object> decryptParameters = new ArrayList<Object>();
			decryptParameters.add(this.pass);
			decryptParameters.add(listSelected);
			componentResources.getContainer().getComponentResources()
					.triggerEvent("eventDecryptListDocFromListDocument",
							decryptParameters.toArray(), null);
			break;	
		case NO_ACTION:
		default:
			break;
		}

		actionbutton = ActionFromBarDocument.NO_ACTION;

		return null;
	}
	
	public Zone onValidateFormFromPasswordDecryptPopup() throws BusinessException {
		String pass = passwordDecryptPopup.getPassword();

		ShareDocumentVo currentSharedDocumentVo=searchDocumentVoByUUid(componentdocuments,currentUuid);
		
		if(null==currentSharedDocumentVo){
			throw new BusinessException(BusinessErrorCode.INVALID_UUID,"invalid uuid for this user");
		}else{
	        boolean copyDone = false;
	        boolean alreadyDownloaded = currentSharedDocumentVo.getDownloaded();
	        
	        //create the copy of the document and remove it from the received documents
	        DocumentVo copyDoc = null;
	        try {
	            copyDoc = shareFacade.createLocalCopy(currentSharedDocumentVo, user);
	            copyDone = true;
	        } catch (BusinessException e) {
	            // process business exception. Can be thrown if no space left or wrong mime type.
	            businessMessagesManagementService.notify(e);
	        }
	        
	        //send an email to the owner if it is the first time the document is downloaded
			if (!alreadyDownloaded) 
				notifyOwnerByEmail(currentSharedDocumentVo);
			
	        if (copyDone) {
	            businessMessagesManagementService.notify(new BusinessUserMessage(BusinessUserMessageType.LOCAL_COPY_OK,
	                MessageSeverity.INFO));
	        }
	        
	        //decrypt document
	        if(copyDoc != null && copyDoc.getEncrypted()){ 
				try {
					documentFacade.decryptDocument(copyDoc, user,pass);
					 businessMessagesManagementService.notify(new BusinessUserMessage(BusinessUserMessageType.DECRYPTION_OK,
				                MessageSeverity.INFO));
	
				} catch (BusinessException e) {
					businessMessagesManagementService.notify(new BusinessUserMessage(BusinessUserMessageType.DECRYPTION_FAILED,
				                MessageSeverity.WARNING));
					logger.debug(e.toString());
				}
	
			}
		}
	    return passwordDecryptPopup.formSuccess();
	}
	
	/***************************
	 * Events 
	 ***************************/
	
	/**
	 * The event triggered by the confirm window when the user pushes on YES.
	 * @throws BusinessException exception throws when the uuid doesn't exist.
	 */
	@OnEvent(value="listDocumentEvent")
	public void removeDocument() throws BusinessException{
		if(null!=currentUuid){
			componentResources.getContainer().getComponentResources().triggerEvent("eventDeleteUniqueFromListDocument", new Object[]{currentUuid}, null);
		}else{
			throw new BusinessException(BusinessErrorCode.INVALID_UUID,"invalid uuid");
		}
	}

	
	@OnEvent(value="signatureDocumentEvent")
	public void signatureDocument() throws BusinessException{
		if(null!=currentUuid){
			componentResources.getContainer().getComponentResources().triggerEvent("eventSignatureUniqueFromListDocument", new Object[]{currentUuid}, null);
		}else{
			throw new BusinessException(BusinessErrorCode.INVALID_UUID,"invalid uuid");
		}
	}

    
	public void copyFromListDocument(List<ShareDocumentVo> shares) {
		logger.debug("copyFromListDocument");
		 
		for (ShareDocumentVo shareDocumentVo:shares){
	        boolean copyDone = false;
	        
	        //create the copy of the document and remove it from the received documents
	        try {
	            shareFacade.createLocalCopy(shareDocumentVo, user);
	            copyDone = true;
	        } catch (BusinessException e) {
	            // process business exception. Can be thrown if no space left or wrong mime type.
	            businessMessagesManagementService.notify(e);
	        }
	        if (copyDone) {
	            businessMessagesManagementService.notify(new BusinessUserMessage(
	            		BusinessUserMessageType.LOCAL_COPY_OK, MessageSeverity.INFO));
	        }
		}
        componentResources.triggerEvent("resetListFiles", null, null);
	}
	
	
	
	/***************************
	 * Other methods
	 ****************************/
	
	/**
	 * Property used for know if the list is empty.
	 * @return true if the list is empty. else false.
	 * 
	 */
	public boolean isEmptyList(){
		return (null == shareDocuments || shareDocuments.isEmpty());
	}
	
	/**
	 * Format the creation date for good displaying using DateFormatUtils of apache commons lib.
	 * @return creation date the date in localized format.
	 */
	public String getCreationDate(){
		SimpleDateFormat formatter = new SimpleDateFormat(messages.get("global.pattern.timestamp"));
		return formatter.format(shareDocument.getCreationDate().getTime());
	}

	/**
	 * Format the creation date for good displaying using DateFormatUtils of apache commons lib.
	 * @return creation date the date in localized format.
	 */
	public String getExpirationDate(){
	   SimpleDateFormat formatter = new SimpleDateFormat(messages.get("global.pattern.timestamp"));
	   return formatter.format(shareDocument.getShareExpirationDate().getTime());
	}
	
	public String getSharingDate(){
		SimpleDateFormat formatter = new SimpleDateFormat(messages.get("global.pattern.timestamp"));
		return formatter.format(shareDocument.getSharingDate().getTime());
	}
	
	public String getFriendlySize(){
		return FileUtils.getFriendlySize(shareDocument.getSize(),messages);
	}
	public String getSharedBy(){
		return shareDocument.getSender().getFirstName()+" "+shareDocument.getSender().getLastName();
	}

	public boolean isDocumentSignedByCurrentUser() throws BusinessException{
		return shareFacade.isSignedShare(user, shareDocument);
	}
	public boolean isDocumentNotSignedByCurrentUserAndDocNotEncrypted() throws BusinessException{
		return !shareDocument.getEncrypted() && !isDocumentSignedByCurrentUser();
	}
	
	public boolean isDocumentSigned(){
		return shareFacade.isSignedShare(user, shareDocument);
	}

	/**
	 * 
	 * @return false (the document is never filesSelected by default)
	 */
	public boolean isFilesSelected() {
		return false;
	}

	/**
	 * This method is called when the form is submitted.
	 * 
	 * @param filesSelected
	 *            filesSelected or not in the form.
	 */
	public void setFilesSelected(boolean selected) {
		if (selected) {
			listSelected.add(shareDocument);
		}
	}
	
	/**
	 * Help method for use in this component. It retrieves a documentVo by it's id.
	 * @param documents list of documents.  
	 * @param uuid the uuid of the document to retrieve.
	 * @return DocumentVo concerned by the search.
	 */
	private ShareDocumentVo searchDocumentVoByUUid(List<ShareDocumentVo> documents,String uuid){
		for(ShareDocumentVo share:documents){
			if(uuid.equals(share.getIdentifier())){
				return share;
			}
		}
		return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@OnEvent(value="eventReorderList")
	public void reorderList(Object[] o1){
		if(o1!=null && o1.length>0){
			this.docs=(List<ShareDocumentVo>)Arrays.copyOf(o1,1)[0];
			this.sorterModel=new SharedFileSorterModel(this.docs);
			refreshFlag=true;
    	}
		
	}
	

	/**
	 * model for the datagrid
	 * we need it to switch off the signature column dynamically
	 * administration can deactivate the signature function
	 * @return
	 * @throws BusinessException
	 */
	public BeanModel initModel() throws BusinessException {
		
		
		//Initialize the sorter model for sorter component.
		if (refreshFlag) {
			shareDocuments = docs;
			refreshFlag = false;
		}
        
		sorterModel = new SharedFileSorterModel(shareDocuments);
    	
    	model = beanModelSource.createDisplayModel(ShareDocumentVo.class, componentResources.getMessages());
        
		
    	
    	
    	// Native TML in HTML was:
		// exclude="fileName, identifier, size, encrypted, ownerLogin, shared, type, shareActive, downloaded, comment"
		// add="fileProperties,expirationDate,signed,actions"

    	// Another native TML in HTML was: 
		// exclude="identifier, size, encrypted, ownerLogin, shared, type, shareActive, downloaded, comment"
		// add="friendlySize,createDate,expirationDate,signed,sharedBy,actions"
	
    	model.add("fileProperties",null);
        //model.add("expirationDate",null);
    	model.add("shareEdit",null);
		model.add("selectedValue", null);
//    	model.add("actions",null);
    	
    	if(activeSignature && activeEncypher){
        	model.add("signed",null);
    		model.add("encryptedAdd", null);
        	model.reorder("fileProperties","expirationDate","shareEdit","signed","encryptedAdd", "selectedValue");
    	} else if (activeSignature){
        	model.add("signed",null);
        	model.reorder("fileProperties","expirationDate","shareEdit","signed", "selectedValue");
		} else  if (activeEncypher){
			model.add("encryptedAdd", null);
	    	model.reorder("fileProperties","expirationDate","shareEdit","encryptedAdd", "selectedValue");
		} else {
			model.reorder("fileProperties","expirationDate","shareEdit","selectedValue");
		}
        return model;
    }

	public boolean isActiveSignature() {
		return activeSignature;
	}
	
	public boolean isEnabledToUpload() {
		return user.isUpload();
	}


	public Link getThumbnailPath() {
        return componentResources.createEventLink("thumbnail", shareDocument.getIdentifier());
	}
	
	public boolean getThumbnailExists() {
		return shareFacade.shareHasThumbnail(user, shareDocument.getIdentifier());
	}
	
	public String getTypeCSSClass() {
		String ret = shareDocument.getType();
		ret = ret.replace("/", "_");
		ret = ret.replace("+", "__");
		ret = ret.replace(".", "_-_");
		return ret;
	}
	
	public void onThumbnail(String docID) {
		InputStream stream=null;
		DocumentVo currentDocumentVo = searchDocumentVoByUUid(componentdocuments,
				docID);
		stream = shareFacade.getShareThumbnailStream(user, currentDocumentVo.getIdentifier());
		if (stream == null)
			return;
		OutputStream os = null;
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		try {
			os = response.getOutputStream("image/png");
			BufferedImage bufferedImage=ImageIO.read(stream);
			if (bufferedImage != null)
				ImageIO.write(bufferedImage, Constants.THMB_DEFAULT_FORMAT, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * remove all carriage return for chenille kit tool tip
	 * @return
	 */
	public String getFormatedComment() {
		String result = shareDocument.getFileComment().replaceAll("\r","");
		result = result.replaceAll("\n", " ");
		return result;
	}
	
	public Zone onActionFromShareEditProperties(String persistenceId) throws BusinessException {
		shareEditForm.setEditShareWithId(persistenceId);
	    return shareEditForm.getShowPopupWindow();
	}
}
