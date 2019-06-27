package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.dao.CmsPageRespository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
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

    //自定义条件查询
    @Test
    public void  testfindAll(){

        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //页面别名模糊查询，需要自定义字符串的匹配器实现模糊查询
        //ExampleMatcher.GenericPropertyMatchers.contains() 包含
        //ExampleMatcher.GenericPropertyMatchers.startWith() 开头匹配
        //条件值
        CmsPage cmsPage = new CmsPage();
        //站点id
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //模板id
        cmsPage.setTemplateId("5a962c16b00ffc514038fafd");
        //页面别名
        cmsPage.setPageAliase("分类导航");

        //创建条件实例
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Pageable pageable = new  PageRequest(0,10);
        Page<CmsPage> all = cmsPageRespository.findAll(example, pageable);
        System.out.println(all.getContent());
        System.out.println(all.getTotalElements());
    }


}
