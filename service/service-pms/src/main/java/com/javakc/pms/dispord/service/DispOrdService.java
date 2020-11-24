package com.javakc.pms.dispord.service;

import com.javakc.commonutils.jpa.base.service.BaseService;
import com.javakc.commonutils.jpa.dynamic.SimpleSpecificationBuilder;
import com.javakc.pms.dispord.dao.DispOrdDao;
import com.javakc.pms.dispord.entity.DispOrd;
import com.javakc.pms.dispord.vo.DispOrdQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DispOrdService extends BaseService<DispOrdDao, DispOrd> {

    @Autowired
    private DispOrdDao dispOrdDao;

    /**
     * 查询所有调度指令库
     * @return
     */
    public List<DispOrd> findAll() {
        return dispOrdDao.findAll();
    }

    public Page<DispOrd> findPageDispOrd(DispOrdQuery dispOrdQuery,int pageNum,int pageSize){
        SimpleSpecificationBuilder<DispOrd> dispOrdSimpleSpecificationBuilder=new SimpleSpecificationBuilder<>();
        if (!StringUtils.isEmpty(dispOrdQuery.getOrderName())){
            dispOrdSimpleSpecificationBuilder.and("orderName",":",dispOrdQuery.getOrderName());
        }
        Page page=dao.findAll(dispOrdSimpleSpecificationBuilder.getSpecification(), PageRequest.of(pageNum-1,pageSize));
        return page;
    }
}