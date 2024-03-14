import Cookies from "js-cookie";
import request from "@/utils/request";
import {removeToken} from "@/utils/auth";

export function checkAdmin() {
    if (Cookies.get("Admin-Token") != null) {
        const parseAdmin = JSON.parse(Cookies.get("admin"));
        const id = parseAdmin.id;
        request.get('/check/' + id).then(res => {
            if (res.code === 403) {
                Cookies.remove("admin")
                removeToken()
                this.$router.push('/login')
                this.$notify.warning(res.msg)
            }
            if (res.code === 202) {
                Cookies.remove("admin")
                removeToken()
                this.$router.push('/login')
                this.$notify.warning(res.msg)
            }
            //
            // if (res.code === 202) {
            //     Cookies.remove("admin")
            //     removeToken()
            //     this.$router.push('/login')
            //     this.$notify.warning(res.msg)
            // }else if (res.code === 403) {
            //     this.$router.push('/login')
            //     this.$notify.warning(res.msg)
            // }
        })
    }
}

