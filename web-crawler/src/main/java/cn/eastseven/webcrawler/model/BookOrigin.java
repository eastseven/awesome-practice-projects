package cn.eastseven.webcrawler.model;

public enum BookOrigin {

    CHINA_PUB(0), WIN_XUAN(1), DANG_DANG(2);

    private int code;

    BookOrigin(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
