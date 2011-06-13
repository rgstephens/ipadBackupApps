package ipadbackupapps

class AppDetails {
    String  name
    String  author
    Date    useDate
    Integer value
    Integer activeTime 
    Integer backgroundActiveTime
    Integer launchCount
    Integer activationCount
    Integer backupFileSize

    static constraints = {
       name(blank:false)
    }
}
