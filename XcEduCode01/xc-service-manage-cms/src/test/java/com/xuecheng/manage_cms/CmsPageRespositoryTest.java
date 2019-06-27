package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.dao.CmsPageRespository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRespositoryTest {

    @Autowired
    CmsPageRespository cmsPageRespository;

    //分页查询测试
    @Test
    public void testFindPage(){
        int page = 0;//从0开始
        int size = 10;//每页记录数10
        //Request实现了Pageable
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRespository.findAll(pageable);
        System.out.println(all.getContent());
        System.out.println(all.getTotalElements());
    }

    //添加页面
    @Test
    public void  testAdd(){

        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("s01");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());
        ArrayList<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("param1");
        cmsPageParam.setPageParamValue("value1");
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        cmsPageRespository.save(cmsPage);
        System.out.println(cmsPage);
        //保存后自动生成了id
        //CmsPage(siteId=s01, pageId=5d142e9d6acd1734e82eac3b, pageName=测试页面, pageAliase=null, pageWebPath=null, pageParameter=null, pagePhysicalPath=null, pageType=null, pageTemplate=null, pageHtml=null, pageStatus=null, pageCreateTime=Thu Jun 27 10:49:01 CST 2019, templateId=t01, pageParams=[CmsPageParam(pageParamName=param1, pageParamValue=value1)], htmlFileId=null, dataUrl=null)
    }

    //删除页面
    @Test
    public void testDelete(){
        //删除之前最好先查一下
        cmsPageRespository.deleteById("5d142e9d6acd1734e82eac3b");
    }

    //修改
    @Test
    public void testUpdate(){
        Optional<CmsPage> optional = cmsPageRespository.findById("5d142e9d6acd1734e82eac3b");
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("测试页面01");
            cmsPageRespository.save(cmsPage);
        }
    }
}
