package cn.eastseven.web;

import cn.eastseven.datatables.mapping.DataTablesInput;
import cn.eastseven.datatables.mapping.DataTablesOutput;
import cn.eastseven.mapper.AreaMapper;
import cn.eastseven.model.Area;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by dongqi on 17/5/27.
 */
@Slf4j
@Controller
public class IndexController {

    @Autowired
    AreaMapper areaMapper;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @JsonView(DataTablesOutput.View.class)
    @PostMapping("/data/area")
    @ResponseBody
    public DataTablesOutput<Area> areaDataTablesOutput(@Valid @RequestBody DataTablesInput input) {
        log.debug("input={}", input);

        Page<Area> page = (Page<Area>) areaMapper.page(input.getDraw(), input.getLength());
        DataTablesOutput<Area> output = new DataTablesOutput<Area>();
        output.setDraw(input.getDraw());
        output.setData(page.getResult());
        output.setRecordsTotal(page.getTotal());
        output.setRecordsFiltered(page.getTotal());
        log.debug("output={}", output);
        return output;
    }
}
