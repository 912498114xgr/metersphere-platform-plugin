package io.metersphere.platform.client;

import im.metersphere.plugin.exception.MSPluginException;
import im.metersphere.plugin.utils.LogUtil;
import io.metersphere.platform.api.BaseClient;
import io.metersphere.platform.domain.FullGoalIssue;
import io.metersphere.platform.domain.FullGoalAddIssueResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;

/**
 * 请求效能平台的客户端
 * HTTP接口层
 */
public class FullGoalClient extends BaseClient {

    protected static final String PREFIX = "/rest/api/2";

    protected String ENDPOINT;

    protected String USER_NAME;

    protected String PASS_WD;

    /**
     * 获取缺陷
     *
     * @param issuesId 缺陷id
     * @return 效能缺陷实体
     */
    public FullGoalIssue getIssues(String issuesId) {
        LogUtil.info("getIssues: " + issuesId);
        ResponseEntity<String> responseEntity = restTemplate.exchange(getBaseUrl() + "/issue/" + issuesId,
                HttpMethod.GET, getAuthHttpEntity(), String.class);
        return (FullGoalIssue) getResultForObject(FullGoalIssue.class, responseEntity);
    }

    /**
     * 添加缺陷
     *
     * @param body 入参
     * @return Response
     */
    public FullGoalAddIssueResponse addIssue(String body) {
        LogUtil.info("addIssue: " + body);
        HttpHeaders headers = getAuthHeader();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(getBaseUrl() + "/issue", HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
            MSPluginException.throwException(e.getMessage());
        }
        return (FullGoalAddIssueResponse) getResultForObject(FullGoalAddIssueResponse.class, response);
    }

    /**
     * 更新缺陷
     *
     * @param id   缺陷id
     * @param body 入参
     */
    public void updateIssue(String id, String body) {
        LogUtil.info("addIssue: " + body);
        HttpHeaders headers = getAuthHeader();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        try {
            restTemplate.exchange(getBaseUrl() + "/issue/" + id, HttpMethod.PUT, requestEntity, String.class);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
            MSPluginException.throwException(e.getMessage());
        }
    }

    /**
     * 删除缺陷
     *
     * @param id 缺陷id
     */
    public void deleteIssue(String id) {
        LogUtil.info("deleteIssue: " + id);
        try {
            restTemplate.exchange(getBaseUrl() + "/issue/" + id, HttpMethod.DELETE, getAuthHttpEntity(), String.class);
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() != 404) {// 404说明jira没有，可以直接删
                MSPluginException.throwException(e.getMessage());
            }
        }
    }

    private String getBaseUrl() {
        return ENDPOINT + PREFIX;
    }

    protected HttpEntity<MultiValueMap> getAuthHttpEntity() {
        return new HttpEntity<>(getAuthHeader());
    }

    protected HttpHeaders getAuthHeader() {
        return getBasicHttpHeaders(USER_NAME, PASS_WD);
    }

    /**
     * 上传附件
     *
     * @param objectId
     * @param file
     */
    public void uploadAttachment(String objectId, File file) {
        // 登录
//        String sessionId = login();
        HttpHeaders authHeader = new HttpHeaders();
        authHeader.setContentType(MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        FileSystemResource fileResource = new FileSystemResource(file);
        paramMap.add("files", fileResource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(paramMap, authHeader);

        // TODO 此处url不确定 请求方式也不确定
        try {
            restTemplate.exchange(getBaseUrl() + "/issue" + "/attachments", HttpMethod.POST, requestEntity,
                    String.class, objectId);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    /**
     * 删除附件
     *
     * @param id 附件id
     */
    public void deleteAttachment(String id) {
        LogUtil.info("deleteAttachment: " + id);
        try {
            restTemplate.exchange(getBaseUrl() + "/attachment/" + id, HttpMethod.DELETE, getAuthHttpEntity(), String.class);
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() != 404) {
                MSPluginException.throwException(e.getMessage());
            }
        }
    }

}
