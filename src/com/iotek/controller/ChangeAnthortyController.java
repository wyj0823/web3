package com.iotek.controller;

import com.iotek.dao.AnthortyInfoDao;
import com.iotek.dao.RoleInfoDao;
import com.iotek.entity.AnthortyInfo;
import com.iotek.entity.RoleInfo;
import com.iotek.service.AnthortyChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChangeAnthortyController {
    @Autowired
    private RoleInfoDao roleInfoDao;
    @Autowired
    private AnthortyInfoDao anthortyInfoDao;
    @Autowired
    private  AnthortyChangeService anthortyChangeService;
    @RequestMapping("/roleInfo")
    public String role(Model model){
        List<RoleInfo> roleInfoList = roleInfoDao.getAll();

             model.addAttribute("roleInfoList", roleInfoList);

        return "system/anthotychange/anthorty_changelist";
    }
    @RequestMapping("/anthortyChange")
    public String anthortyChange(Model model,RoleInfo roleInfo){
             model.addAttribute("roleInfo",roleInfo);
             Map<AnthortyInfo,List<AnthortyInfo>> map =new HashMap<>();

        List<AnthortyInfo> infoList = anthortyInfoDao.queryAllAnthorty();


        for (int i = 0; i <infoList.size(); i++) {
         AnthortyInfo   anthortyInfo=infoList.get(i);
            if (anthortyInfo.getAnthortyPid()==1){
                map.put(anthortyInfo,new ArrayList<AnthortyInfo>());

            }else{

                for (AnthortyInfo Parentinfo :map.keySet()) {

                    if (Parentinfo.getAnthortyId()==anthortyInfo.getAnthortyPid()){
                        map.get(Parentinfo).add(anthortyInfo);
                    }
                }

            }
        }
        //用于 判断  变更权限时 显示  已默认的权限
        List<Integer> idList = roleInfoDao.getAnthortyByRoleId(roleInfo.getRoleId());
        //用于 判断  变更权限时 显示  已默认的权限
         model.addAttribute("idList",idList);

        model.addAttribute("map",map);

        return "system/anthotychange/anthorty_change";
    }
    //用于 处理 权限 更新
@RequestMapping("/sucessw")
    public  String  Sucess(int roleId,int[] anthortyId){

    anthortyChangeService.change(roleId,anthortyId);
        return "forward:roleInfo";
}

}
