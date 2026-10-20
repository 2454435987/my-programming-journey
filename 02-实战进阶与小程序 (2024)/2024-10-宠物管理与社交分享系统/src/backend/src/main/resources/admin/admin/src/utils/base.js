const base = {
    get() {
        return {
            url : "http://localhost:8080/springboot2zemx/",
            name: "springboot2zemx",
            // 退出到首页链接
            indexUrl: 'http://localhost:8080/springboot2zemx/front/index.html'
        };
    },
    getProjectName(){
        return {
            projectName: "宠物管理与社交分享系统"
        } 
    }
}
export default base
