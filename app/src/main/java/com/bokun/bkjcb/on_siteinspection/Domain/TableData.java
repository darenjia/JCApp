package com.bokun.bkjcb.on_siteinspection.Domain;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/10/13.
 */

public class TableData {
    private ArrayList<BiaogeInfo> BiaogeInfo;
    private ArrayList<ShengChan1> Shengchan01;
    private ArrayList<ShengChan2> Shengchan02;
    private ArrayList<JingGao> Jinggao;
    private ArrayList<ShiYongDW> ScShiyongdw;
    private ArrayList<ShiYongYongTu> ScShiyong;

    public ArrayList<BiaogeInfo> getBiaogeInfo() {
        return BiaogeInfo;
    }

    public void setBiaogeInfo(ArrayList<BiaogeInfo> biaogeInfo) {
        BiaogeInfo = biaogeInfo;
    }

    public ArrayList<ShengChan1> getShengchan01() {
        return Shengchan01;
    }

    public void setShengchan01(ArrayList<ShengChan1> shengchan01) {
        Shengchan01 = shengchan01;
    }

    public ArrayList<ShengChan2> getShengchan02() {
        return Shengchan02;
    }

    public void setShengchan02(ArrayList<ShengChan2> shengchan02) {
        Shengchan02 = shengchan02;
    }

    public ArrayList<JingGao> getJinggao() {
        return Jinggao;
    }

    public void setJinggao(ArrayList<JingGao> jinggao) {
        Jinggao = jinggao;
    }

    public ArrayList<ShiYongDW> getScShiyongdw() {
        return ScShiyongdw;
    }

    public void setScShiyongdw(ArrayList<ShiYongDW> scShiyongdw) {
        ScShiyongdw = scShiyongdw;
    }

    public ArrayList<ShiYongYongTu> getScShiyong() {
        return ScShiyong;
    }

    public void setScShiyong(ArrayList<ShiYongYongTu> scShiyong) {
        ScShiyong = scShiyong;
    }

    public static class BiaogeInfo {


        /**
         * ScGgGdBgFzr : 张翼
         * ScGgGdBgTbr : 黄昌海
         * ScGgGdBgLxdh : 34320786
         * ScGgGdBgBcrq : 2006/10/30
         */

        private int SysId;
        private String ScGgGdBgFzr;
        private String ScGgGdBgTbr;
        private String ScGgGdBgLxdh;
        private String ScGgGdBgBcrq;

        public String getScGgGdBgFzr() {
            return ScGgGdBgFzr;
        }

        public void setScGgGdBgFzr(String ScGgGdBgFzr) {
            this.ScGgGdBgFzr = ScGgGdBgFzr;
        }

        public String getScGgGdBgTbr() {
            return ScGgGdBgTbr;
        }

        public void setScGgGdBgTbr(String ScGgGdBgTbr) {
            this.ScGgGdBgTbr = ScGgGdBgTbr;
        }

        public String getScGgGdBgLxdh() {
            return ScGgGdBgLxdh;
        }

        public void setScGgGdBgLxdh(String ScGgGdBgLxdh) {
            this.ScGgGdBgLxdh = ScGgGdBgLxdh;
        }

        public String getScGgGdBgBcrq() {
            return ScGgGdBgBcrq;
        }

        public void setScGgGdBgBcrq(String ScGgGdBgBcrq) {
            this.ScGgGdBgBcrq = ScGgGdBgBcrq;
        }

        public int getSysId() {
            return SysId;
        }

        public void setSysId(int sysId) {
            SysId = sysId;
        }
    }

    public static class JingGao {


        /**
         * SysId : 0
         * SysGcxxdjh : 0
         * ScGgSzlc : 1
         * ScGgLcjg : 2.2～2.8M
         * SeqId : 0
         * Shengchan01 : 0
         * SysTime : 2017-10-12T00:00:00+08:00
         * IsChanged : true
         */

        private int SysId;
        private int ScGgSzlc;
        private String ScGgLcjg;

        public int getSysId() {
            return SysId;
        }

        public void setSysId(int sysId) {
            SysId = sysId;
        }

        public int getScGgSzlc() {
            return ScGgSzlc;
        }

        public void setScGgSzlc(int ScGgSzlc) {
            this.ScGgSzlc = ScGgSzlc;
        }

        public String getScGgLcjg() {
            return ScGgLcjg;
        }

        public void setScGgLcjg(String ScGgLcjg) {
            this.ScGgLcjg = ScGgLcjg;
        }
    }

    public static class ShengChan1 {

        /**
         * SysId : 0
         * SysGcxxdjh : 10010185         登记号
         * SysMfgcbh : null              民防工程编号
         * ScJbGcmc : 锦绣江南家园陶然居    工程名称
         * ScJbGcdz : 金汇南路60弄         工程地址
         * ScJbSzqx : 闵行区               所在区
         * ScJbJdxz : 虹桥镇               街道乡镇
         * ScJbBz : null                  备注
         * ScXzJcnf : 2002                检查年份
         * ScXzJzmj : 1795                建筑面积
         * ScXzSjyt : 机动车库              设计用途
         * ScXzMfjzmj : 1795               民防建筑面积
         * ScXzMzsd : 2.2                   埋置深度
         * ScXzKbs : 2                      出入口数
         * ScXzDxcs : 1                     地下层数
         * ScXzJgys : 1                     竣工验收
         * ScXzGcxs : 单建                    工程形式
         * ScXzJglb : 钢筋混凝土               结构类别
         * ScXzGclb : 地下室                   工程类别
         * ScXzLxpzjb : null                  立项批准级别
         * ScXzGcxz : 1                         工程性质
         * ScXzYllt : 0                         连通
         * ScXzLtqtgc : 0                       是否连通
         * ScXzLtdxZb : 0                        连通周边
         * ScXzLtdxDt : null                    连通地铁
         * ScXzYy : 1
         * ScXzXz : 0
         * ScXzFq : 0
         * ScXzLyjz : 0
         * ScXzYymj : 1795
         * ScXzXzmj : 0
         * ScXzFqmj : 0
         * ScDaZlh : null
         * ScXzDmjz : null              地面建筑情况
         * ScXzDmjzQt : null            地面建筑情况（其他）
         * ScXzSsgc : 0                 立项或批准级别
         */

        private int SysGcxxdjh;
        private int SysId;
        private String SysMfgcbh;
        private String ScJbGcmc;
        private String ScJbGcdz;
        private String ScJbSzqx;
        private String ScJbJdxz;
        private String ScJbBz;
        private String ScXzJcnf;
        private int ScXzJzmj;
        private String ScXzSjyt;
        private int ScXzMfjzmj;
        private double ScXzMzsd;
        private int ScXzKbs;
        private int ScXzDxcs;
        private int ScXzJgys;
        private String ScXzGcxs;
        private String ScXzJglb;
        private String ScXzGclb;
        //    private Object ScXzLxpzjb;
        private int ScXzGcxz;
        private int ScXzYllt;
        private int ScXzLtqtgc;
        private int ScXzLtdxZb;
        private String ScXzLtdxDt;
        /*  private int ScXzYy;
          private int ScXzXz;
          private int ScXzFq;
          private int ScXzLyjz;
          private int ScXzYymj;
          private int ScXzXzmj;
          private int ScXzFqmj;
          private Object ScDaZlh;*/
        private String ScXzDmjz;
        private String ScXzDmjzQt;
        private int ScXzSsgc;

        public int getSysId() {
            return SysId;
        }

        public void setSysId(int sysId) {
            SysId = sysId;
        }

        public int getSysGcxxdjh() {
            return SysGcxxdjh;
        }

        public void setSysGcxxdjh(int sysGcxxdjh) {
            SysGcxxdjh = sysGcxxdjh;
        }

        public String getSysMfgcbh() {
            return SysMfgcbh;
        }

        public void setSysMfgcbh(String sysMfgcbh) {
            SysMfgcbh = sysMfgcbh;
        }

        public String getScJbGcmc() {
            return ScJbGcmc;
        }

        public void setScJbGcmc(String scJbGcmc) {
            ScJbGcmc = scJbGcmc;
        }

        public String getScJbGcdz() {
            return ScJbGcdz;
        }

        public void setScJbGcdz(String scJbGcdz) {
            ScJbGcdz = scJbGcdz;
        }

        public String getScJbSzqx() {
            return ScJbSzqx;
        }

        public void setScJbSzqx(String scJbSzqx) {
            ScJbSzqx = scJbSzqx;
        }

        public String getScJbJdxz() {
            return ScJbJdxz;
        }

        public void setScJbJdxz(String scJbJdxz) {
            ScJbJdxz = scJbJdxz;
        }

        public String getScJbBz() {
            return ScJbBz;
        }

        public void setScJbBz(String scJbBz) {
            ScJbBz = scJbBz;
        }

        public String getScXzJcnf() {
            return ScXzJcnf;
        }

        public void setScXzJcnf(String scXzJcnf) {
            ScXzJcnf = scXzJcnf;
        }

        public int getScXzJzmj() {
            return ScXzJzmj;
        }

        public void setScXzJzmj(int scXzJzmj) {
            ScXzJzmj = scXzJzmj;
        }

        public String getScXzSjyt() {
            return ScXzSjyt;
        }

        public void setScXzSjyt(String scXzSjyt) {
            ScXzSjyt = scXzSjyt;
        }

        public int getScXzMfjzmj() {
            return ScXzMfjzmj;
        }

        public void setScXzMfjzmj(int scXzMfjzmj) {
            ScXzMfjzmj = scXzMfjzmj;
        }

        public double getScXzMzsd() {
            return ScXzMzsd;
        }

        public void setScXzMzsd(double scXzMzsd) {
            ScXzMzsd = scXzMzsd;
        }

        public int getScXzKbs() {
            return ScXzKbs;
        }

        public void setScXzKbs(int scXzKbs) {
            ScXzKbs = scXzKbs;
        }

        public int getScXzDxcs() {
            return ScXzDxcs;
        }

        public void setScXzDxcs(int scXzDxcs) {
            ScXzDxcs = scXzDxcs;
        }

        public int getScXzJgys() {
            return ScXzJgys;
        }

        public void setScXzJgys(int scXzJgys) {
            ScXzJgys = scXzJgys;
        }

        public String getScXzGcxs() {
            return ScXzGcxs;
        }

        public void setScXzGcxs(String scXzGcxs) {
            ScXzGcxs = scXzGcxs;
        }

        public String getScXzJglb() {
            return ScXzJglb;
        }

        public void setScXzJglb(String scXzJglb) {
            ScXzJglb = scXzJglb;
        }

        public String getScXzGclb() {
            return ScXzGclb;
        }

        public void setScXzGclb(String scXzGclb) {
            ScXzGclb = scXzGclb;
        }

        public int getScXzGcxz() {
            return ScXzGcxz;
        }

        public void setScXzGcxz(int scXzGcxz) {
            ScXzGcxz = scXzGcxz;
        }

        public int getScXzYllt() {
            return ScXzYllt;
        }

        public void setScXzYllt(int scXzYllt) {
            ScXzYllt = scXzYllt;
        }

        public int getScXzLtqtgc() {
            return ScXzLtqtgc;
        }

        public void setScXzLtqtgc(int scXzLtqtgc) {
            ScXzLtqtgc = scXzLtqtgc;
        }

        public int getScXzLtdxZb() {
            return ScXzLtdxZb;
        }

        public void setScXzLtdxZb(int scXzLtdxZb) {
            ScXzLtdxZb = scXzLtdxZb;
        }

        public String getScXzLtdxDt() {
            return ScXzLtdxDt;
        }

        public void setScXzLtdxDt(String scXzLtdxDt) {
            ScXzLtdxDt = scXzLtdxDt;
        }

        public String getScXzDmjz() {
            return ScXzDmjz;
        }

        public void setScXzDmjz(String scXzDmjz) {
            ScXzDmjz = scXzDmjz;
        }

        public String getScXzDmjzQt() {
            return ScXzDmjzQt;
        }

        public void setScXzDmjzQt(String scXzDmjzQt) {
            ScXzDmjzQt = scXzDmjzQt;
        }

        public int getScXzSsgc() {
            return ScXzSsgc;
        }

        public void setScXzSsgc(int scXzSsgc) {
            ScXzSsgc = scXzSsgc;
        }
    }

    public static class ShengChan2 {

        /**
         * ScJszDw : 无                     建设单位
         * ScJszSjzg : 无                    上级主管
         * ScJszFddbr : 无                   法定代表人
         * ScJszLxr : 无                     联系人
         * ScJszLxdh : 无                    联系电话
         * ScJszDwxz : 机关                  单位性质
         * ScJszCqz : 0
         * ScSyzMc : 锦绣江南家园全体业主   所有者名称
         * ScSyzCqz : 1                    所有者产权证
         * ScSyzFddbr : null              所有者法定代表人
         * ScSyzLxr : 黄昌海               所有者联系人
         * ScSyzLxdh : 34320786           所有者电话
         * ScSyzLx : 其它                    所有者类型
         * ScGlzMc : 上海东湖物业管理有限公司锦绣江南家园管理处  管理者名称
         * ScGlzSjzg : 上海东湖物业管理有限公司               管理者上级主管单位
         * ScGlzFddbr : 张翼                               管理者法定代表人
         * ScGlzLxr : 张翼                                 管理者联系人
         * ScGlzLxdh : 34321211                           管理者电话
         * ScGlzLx : 企业                                  管理者类型
         */

        private int SysId;
        private String ScJszDw;
        private String ScJszSjzg;
        private String ScJszFddbr;
        private String ScJszLxr;
        private String ScJszLxdh;
        private String ScJszDwxz;
        private int ScJszCqz;
        private String ScSyzMc;
        private int ScSyzCqz;
        private String ScSyzFddbr;
        private String ScSyzLxr;
        private String ScSyzLxdh;
        private String ScSyzLx;
        private String ScGlzMc;
        private String ScGlzSjzg;
        private String ScGlzFddbr;
        private String ScGlzLxr;
        private String ScGlzLxdh;
        private String ScGlzLx;

        public String getScJszDw() {
            return ScJszDw;
        }

        public void setScJszDw(String ScJszDw) {
            this.ScJszDw = ScJszDw;
        }

        public String getScJszSjzg() {
            return ScJszSjzg;
        }

        public void setScJszSjzg(String ScJszSjzg) {
            this.ScJszSjzg = ScJszSjzg;
        }

        public String getScJszFddbr() {
            return ScJszFddbr;
        }

        public void setScJszFddbr(String ScJszFddbr) {
            this.ScJszFddbr = ScJszFddbr;
        }

        public String getScJszLxr() {
            return ScJszLxr;
        }

        public void setScJszLxr(String ScJszLxr) {
            this.ScJszLxr = ScJszLxr;
        }

        public String getScJszLxdh() {
            return ScJszLxdh;
        }

        public void setScJszLxdh(String ScJszLxdh) {
            this.ScJszLxdh = ScJszLxdh;
        }

        public String getScJszDwxz() {
            return ScJszDwxz;
        }

        public void setScJszDwxz(String ScJszDwxz) {
            this.ScJszDwxz = ScJszDwxz;
        }

        public int getScJszCqz() {
            return ScJszCqz;
        }

        public void setScJszCqz(int ScJszCqz) {
            this.ScJszCqz = ScJszCqz;
        }

        public String getScSyzMc() {
            return ScSyzMc;
        }

        public void setScSyzMc(String ScSyzMc) {
            this.ScSyzMc = ScSyzMc;
        }

        public int getScSyzCqz() {
            return ScSyzCqz;
        }

        public void setScSyzCqz(int ScSyzCqz) {
            this.ScSyzCqz = ScSyzCqz;
        }

        public String getScSyzFddbr() {
            return ScSyzFddbr;
        }

        public void setScSyzFddbr(String ScSyzFddbr) {
            this.ScSyzFddbr = ScSyzFddbr;
        }

        public String getScSyzLxr() {
            return ScSyzLxr;
        }

        public void setScSyzLxr(String ScSyzLxr) {
            this.ScSyzLxr = ScSyzLxr;
        }

        public String getScSyzLxdh() {
            return ScSyzLxdh;
        }

        public void setScSyzLxdh(String ScSyzLxdh) {
            this.ScSyzLxdh = ScSyzLxdh;
        }

        public String getScSyzLx() {
            return ScSyzLx;
        }

        public void setScSyzLx(String ScSyzLx) {
            this.ScSyzLx = ScSyzLx;
        }

        public String getScGlzMc() {
            return ScGlzMc;
        }

        public void setScGlzMc(String ScGlzMc) {
            this.ScGlzMc = ScGlzMc;
        }

        public String getScGlzSjzg() {
            return ScGlzSjzg;
        }

        public void setScGlzSjzg(String ScGlzSjzg) {
            this.ScGlzSjzg = ScGlzSjzg;
        }

        public String getScGlzFddbr() {
            return ScGlzFddbr;
        }

        public void setScGlzFddbr(String ScGlzFddbr) {
            this.ScGlzFddbr = ScGlzFddbr;
        }

        public String getScGlzLxr() {
            return ScGlzLxr;
        }

        public void setScGlzLxr(String ScGlzLxr) {
            this.ScGlzLxr = ScGlzLxr;
        }

        public String getScGlzLxdh() {
            return ScGlzLxdh;
        }

        public void setScGlzLxdh(String ScGlzLxdh) {
            this.ScGlzLxdh = ScGlzLxdh;
        }

        public String getScGlzLx() {
            return ScGlzLx;
        }

        public void setScGlzLx(String ScGlzLx) {
            this.ScGlzLx = ScGlzLx;
        }

        public int getSysId() {
            return SysId;
        }

        public void setSysId(int sysId) {
            SysId = sysId;
        }
    }

    public static class ShiYongDW {

        /**
         * SydwMc : 上海东湖物业管理有限公司锦绣江南家园管理处
         * SydwFddbr : 张翼
         * SydwLxr : 黄昌海
         * SydwLxdh : 34320786
         * SydwLx : 企业
         * SydwSyfs : 自用
         * SydwGsdj : 1
         * SydwZaxk : 1
         * SydwWsxk : 1
         * SydwXfxk : 1
         * SeqId : 1
         */

        private int SysId;
        private String SydwMc;
        private String SydwFddbr;
        private String SydwLxr;
        private String SydwLxdh;
        private String SydwLx;
        private String SydwSyfs;
        private int SydwGsdj;
        private int SydwZaxk;
        private int SydwWsxk;
        private int SydwXfxk;

        public String getSydwMc() {
            return SydwMc;
        }

        public void setSydwMc(String SydwMc) {
            this.SydwMc = SydwMc;
        }

        public String getSydwFddbr() {
            return SydwFddbr;
        }

        public void setSydwFddbr(String SydwFddbr) {
            this.SydwFddbr = SydwFddbr;
        }

        public String getSydwLxr() {
            return SydwLxr;
        }

        public void setSydwLxr(String SydwLxr) {
            this.SydwLxr = SydwLxr;
        }

        public String getSydwLxdh() {
            return SydwLxdh;
        }

        public void setSydwLxdh(String SydwLxdh) {
            this.SydwLxdh = SydwLxdh;
        }

        public String getSydwLx() {
            return SydwLx;
        }

        public void setSydwLx(String SydwLx) {
            this.SydwLx = SydwLx;
        }

        public String getSydwSyfs() {
            return SydwSyfs;
        }

        public void setSydwSyfs(String SydwSyfs) {
            this.SydwSyfs = SydwSyfs;
        }

        public int getSydwGsdj() {
            return SydwGsdj;
        }

        public void setSydwGsdj(int SydwGsdj) {
            this.SydwGsdj = SydwGsdj;
        }

        public int getSydwZaxk() {
            return SydwZaxk;
        }

        public void setSydwZaxk(int SydwZaxk) {
            this.SydwZaxk = SydwZaxk;
        }

        public int getSydwWsxk() {
            return SydwWsxk;
        }

        public void setSydwWsxk(int SydwWsxk) {
            this.SydwWsxk = SydwWsxk;
        }

        public int getSydwXfxk() {
            return SydwXfxk;
        }

        public void setSydwXfxk(int SydwXfxk) {
            this.SydwXfxk = SydwXfxk;
        }

        public int getSysId() {
            return SysId;
        }

        public void setSysId(int sysId) {
            SysId = sysId;
        }
    }

    public static class ShiYongYongTu {

        /**
         * SeqId : 0
         * SysId : 0
         * SysTime : 2017-10-12T00:00:00+08:00
         * SysGcxxdjh : 0
         * SyScSsmc : 汽车库
         * SyScJzmj : 1795
         * SyScCws : 70
         * SyScQtyt : null
         * SyScCz : 0
         * SyScRy : 0
         * SyScCw : 0
         * SyScBawh : null
         * SyScMr01 :
         * SyScMr02 :
         * SyScMr03 :
         * Shengchan01 : 0
         * IsChanged : true
         */

        private int SysId;
        private String SyScSsmc;
        private int SyScJzmj;
        private int SyScCws;
        private String SyScQtyt;

        public int getSysId() {
            return SysId;
        }

        public void setSysId(int sysId) {
            SysId = sysId;
        }

        public String getSyScSsmc() {
            return SyScSsmc;
        }

        public void setSyScSsmc(String SyScSsmc) {
            this.SyScSsmc = SyScSsmc;
        }

        public int getSyScJzmj() {
            return SyScJzmj;
        }

        public void setSyScJzmj(int SyScJzmj) {
            this.SyScJzmj = SyScJzmj;
        }

        public int getSyScCws() {
            return SyScCws;
        }

        public void setSyScCws(int SyScCws) {
            this.SyScCws = SyScCws;
        }

        public String getSyScQtyt() {
            return SyScQtyt;
        }

        public void setSyScQtyt(String SyScQtyt) {
            this.SyScQtyt = SyScQtyt;
        }
    }

}
