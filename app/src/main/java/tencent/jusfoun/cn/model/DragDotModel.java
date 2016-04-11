package tencent.jusfoun.cn.model;

/**
 * Author  wangchenchen
 * CreateDate 2015/12/22.
 * Email wcc@jusfoun.com
 * Description
 */
public class DragDotModel extends BaseModel {

    private String name;
    private boolean isShow;
    private boolean isCheck;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
