package cn.eastseven.web;

import cn.eastseven.datatables.mapping.DataTablesInput;
import cn.eastseven.datatables.mapping.DataTablesOutput;
import cn.eastseven.mapper.AreaMapper;
import cn.eastseven.model.Area;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * Created by dongqi on 17/5/27.
 */
@Slf4j
@Controller
public class IndexController {

    @Autowired
    AreaMapper areaMapper;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("regions", areaMapper.groupByRegion());
        //model.addAttribute("provinces", areaMapper.groupByProvince());
        return "index";
    }

    @JsonView(DataTablesOutput.View.class)
    @PostMapping("/data/area")
    @ResponseBody
    public DataTablesOutput<Area> areaDataTablesOutput(@Valid @RequestBody DataTablesInput input, HttpServletRequest request) throws ServletRequestBindingException {
        String code = ServletRequestUtils.getStringParameter(request, "code");
        String region = ServletRequestUtils.getStringParameter(request, "region");

        Map<String, Object> params = Maps.newHashMap();
        if (!StringUtils.isEmpty(code)) params.put("code", code);
        if (!StringUtils.isEmpty(region)) params.put("region", region);

        log.debug("input={}", input);

        PageHelper.startPage(input.getStart() / input.getLength(), input.getLength());
        Page<Area> page = (Page<Area>) areaMapper.page(params);
        DataTablesOutput<Area> output = new DataTablesOutput<Area>();
        output.setDraw(input.getDraw());
        output.setData(page.getResult());
        output.setRecordsTotal(page.getTotal());
        output.setRecordsFiltered(page.getTotal());
        log.debug("output={}", output);
        return output;
    }
}
