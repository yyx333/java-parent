package com.javakc.pms.dispord.service;

import com.javakc.commonutils.jpa.base.service.BaseService;
import com.javakc.commonutils.jpa.dynamic.SimpleSpecificationBuilder;
import com.javakc.pms.dispord.dao.DispOrdDao;
import com.javakc.pms.dispord.entity.DispOrd;
import com.javakc.pms.dispord.vo.DispOrdQuery;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        //创建时间-区间查询
        if (!StringUtils.isEmpty(dispOrdQuery.getBeginDate())){
            dispOrdSimpleSpecificationBuilder.and("gmtCreate","ge",dispOrdQuery.getBeginDate());
        }
        if (!StringUtils.isEmpty(dispOrdQuery.getEndDate())){
            dispOrdSimpleSpecificationBuilder.and("gmtCreate","lt",dispOrdQuery.getEndDate());
        }
        return dao.findAll(dispOrdSimpleSpecificationBuilder.getSpecification(), PageRequest.of(pageNum-1,pageSize));
    }

    /*/导出
    public void exportExcel(HttpServletResponse response){
        //设置表头
        String[] titles={"指令名称","指令类型","优先级","指令描述"};
        //创建Excel工作簿
        HSSFWorkbook hssfWorkbook=new HSSFWorkbook();
        //在当前工作簿下创建sheet
        HSSFSheet hssfSheet=hssfWorkbook.createSheet("指令列表");
        //在当前的sheet下创建Row,第一行代表的是表头
        HSSFRow row=hssfSheet.createRow(0);
        //设置表头数据
        for (int i = 0; i <titles.length ; i++) {
            row.createCell(i).setCellValue(titles[i]);
        }
        //查询所有数据
        List<DispOrd> list=dao.findAll();
        //设置行数据
        for (int i = 0; i <list.size() ; i++) {
            //取出数据
            DispOrd dispOrd=list.get(i);
            HSSFRow hssfRow=hssfSheet.createRow(i+1);
            hssfRow.createCell(0).setCellValue(dispOrd.getOrderName());
            hssfRow.createCell(1).setCellValue(dispOrd.getSpecType());
            hssfRow.createCell(2).setCellValue(dispOrd.getPriority());
            hssfRow.createCell(3).setCellValue(dispOrd.getOrderDesc());
        }

        //以日期为文件名
        String fileName =new String(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        try {
            response.setHeader("Content-disposition","attachment;filename="+fileName+".xls");
            //下载
            ServletOutputStream outputStream=response.getOutputStream();
            hssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //导入
    @Transactional
    public void importExcel(MultipartFile file){
        try {
            //获取文件流
           InputStream inputStream= file.getInputStream();
           //创建工作簿接口
            Workbook workbook=null;
           //把文件流的内容放入到工作簿当中
            if(file.getOriginalFilename().endsWith(".xlsx")){
                //2003年之后 支持xlsx
                workbook=new XSSFWorkbook(inputStream);
            }else {
                //2003年之前 支持xls
                workbook=new HSSFWorkbook(inputStream);
            }
            //得到Sheet总数
            int numberOfSheets=workbook.getNumberOfSheets();
            for (int i = 0; i <numberOfSheets ; i++) {
                //得到具体的Sheet
                Sheet sheet=workbook.getSheetAt(i);
                //得到Row总行数
                int physicalNumberOfRows=sheet.getPhysicalNumberOfRows();
                //创建一个集合
                List<DispOrd> list=new ArrayList<>();
                for (int j = 1; j <physicalNumberOfRows ; j++) {
                    //创建对象
                    DispOrd dispOrd=new DispOrd();
                    Row row=sheet.getRow(j);
                    //设置到对象当中
                    dispOrd.setOrderName(row.getCell(0).getStringCellValue());
                    dispOrd.setSpecType((int)row.getCell(1).getNumericCellValue());
                    dispOrd.setPriority((int)row.getCell(2).getNumericCellValue());
                    dispOrd.setOrderDesc(row.getCell(3).getStringCellValue());

                    //把对象放到一个集合当中
                    list.add(dispOrd);
                }
                //批量保存
                dao.saveAll(list);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}