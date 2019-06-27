package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRespository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PageService {

    @Autowired
    CmsPageRespository cmsPageRespository;

    /**
     *
     * @param page 当前页码
     * @param size 每页显示条数
     * @param queryPageRequest 查询条件
     * @return 页面列表
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest){
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        if(page <= 0){
            page = 1;
        }
        if(size <= 0){
            size = 10;
        }
        page = page -1;//为了适应mongodb接口

        // 处理查询条件
        //1.创建条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //2.创建条件对象
        CmsPage cmsPage = new CmsPage();
        //站点id
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //页面别名
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //3.创建条件实例
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        // 分页对象
        Pageable pageable = new PageRequest(page,size);
        // 分页查询
        Page<CmsPage> all = cmsPageRespository.findAll(example,pageable);
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<>();
        cmsPageQueryResult.setList(all.getContent());
        cmsPageQueryResult.setTotal(all.getTotalElements());
        //返回结果
        return  new QueryResponseResult(CommonCode.SUCCESS,cmsPageQueryResult);
    }

    //新增页面
    public CmsPageResult add(CmsPage cmsPage){
        //校验页面是否存在 根据页面名称、页面站点、页面webpath查询
        CmsPage cmsPage1 = cmsPageRespository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),
                cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(cmsPage1 == null){
            cmsPage.setPageId(null);//添加页面主键有spring data自动生成
            cmsPageRespository.save(cmsPage);
            //返回结果
            CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, cmsPage);
            return cmsPageResult;
        }
        return new CmsPageResult(CommonCode.FAIL,cmsPage);
    }

    public CmsPage findById(String id){
        Optional<CmsPage> optional = cmsPageRespository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        //返回空
        return null;
    }

    //更新页面
    public  CmsPageResult update(String id,CmsPage cmsPage){
        //根据id查询页面
        CmsPage one = this.findById(id);
        if(one != null){
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新站点id
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //执行更新
            CmsPage save = cmsPageRespository.save(one);
            if (save != null){
                return  new CmsPageResult(CommonCode.SUCCESS,save);
            }
        }
        //返回失败
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    //根据页面id删除页面
    public ResponseResult deleteById(String id){
        //删除之前先根据id去查一下
        CmsPage one = this.findById(id);
        if(one != null){
            cmsPageRespository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);

    }
}
