<script>
export default {
  onLaunch() {
    // #ifdef H5
    this.autoDemoLogin()
    // #endif
    console.log('App Launch')
  },
  methods: {
    /**
     * H5 预览演示登录:
     * URL hash 中带 auth=elder|volunteer|admin 时自动写入演示身份,
     * 便于截图与演示; 真实小程序使用微信授权登录。
     */
    autoDemoLogin() {
      try {
        const hash = window.location.hash || ''
        const search = window.location.search || ''
        const m = (hash + search).match(/auth=(elder|volunteer|admin)/)
        if (!m) return
        const map = {
          elder: { id: 2, role: 'elder', real_name: '张桂芳', openid: 'wx_openid_elder_001' },
          volunteer: { id: 6, role: 'volunteer', real_name: '陈志强', openid: 'wx_openid_vol_001' },
          admin: { id: 1, role: 'admin', real_name: '马卫国', openid: 'wx_openid_admin_001' }
        }
        const u = map[m[1]]
        if (u) {
          uni.setStorageSync('currentUser', u)
        }
      } catch (e) {
        /* ignore */
      }
    }
  }
}
</script>

<style>
page {
  background: #f5f7fa;
  font-size: 28rpx;
  color: #333;
}
</style>
