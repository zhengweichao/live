package top.vchao.live.pro.excel;

/**
 * @ Create_time: 2018/7/13 on 16:11.
 * @ description：
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
class BillObject {
    private String food;
    private String clothes;
    private String house;

    public BillObject() {
    }

    public BillObject(String food, String clothes, String house) {
        this.food = food;
        this.clothes = clothes;
        this.house = house;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getClothes() {
        return clothes;
    }

    public void setClothes(String clothes) {
        this.clothes = clothes;
    }
}
