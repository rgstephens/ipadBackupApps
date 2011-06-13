package ipadbackupapps
import groovy.sql.Sql

class IpadScalarsController {
    
    def defaultAction = 'importIpad'
    
    def importIpad = {
        log.info('Start import')
        def sql = Sql.newInstance("jdbc:sqlite:/home/greg/ipad.sqlite", "", "", "org.sqlite.JDBC") 
        def importHTML = ""
        def appName = ""
	def prevAppName = ""
	def locDate = new Date()
	def app = new AppDetails()
        def scalar  = sql.eachRow("select * from Scalars"){
            println it.key
            if (it.key.substring(0,3) != 'com') {
	        def tempKey = it.key.tokenize('.')
		appName = tempKey[3]
		// if the appName has changed then write the record and clear it
		if ((appName != prevAppName) && (prevAppName != "")) {
		   importHTML = importHTML + prevAppName + " by " + app.author + ",  launches: " + app.launchCount + ", Date: " + String.format('%td %<tb %<tY', app.useDate) + "<br>"
		   app.save(flush:true)
		}
		locDate.time = 0 // long 0 is beginning of 1 Jan 1970 GMT
		app.useDate = locDate + it.daysSince1970
		app.author = tempKey[2]
		switch(tempKey[0]) {
		  case "appActivationCount":
		     app.activationCount = it.value
		  case "appLaunchCount":
		     app.launchCount = it.value
		  case "appActiveTime":
		     app.activeTime = it.value
		  case "appBackgroundActiveTime":
		     app.backgroundActiveTime = it.value
		  case "appBackupFileSize":
		     app.backupFileSize = it.value
		} // end switch
		prevAppName = appName
	     } // if ! 'com'
        } // end eachRow
        render importHTML
        log.info('End import')
    }

    // def scaffold = IpadScalars
    
    def test = {
       log.info('Start test')
       // beans.dataSource.url = "/home/greg/ipad.sqlite"
       IpadScalars.withSession { session ->
          def allScalars = IpadScalars.list()
       	  def scalarsCount = IpadScalars.count()
       	  println "Records: " + scalarsCount
	  def scalarsHTML = "Records: " + scalarsCount + "<p>"
	  def appName = ""
	  def prevAppName = ""
	  def locDate = new Date()
	  def app = new AppDetails()
	  for (scalar in allScalars) {
       	  //   println "key: " + scalar.key
	     // scalarsHTML = scalarsHTML + "key: " + scalar.key + "<br>"
             if (scalar.key.substring(0,3) != 'com') {
	        def tempKey = scalar.key.tokenize('.')
		appName = tempKey[3]
		// if the appName has changed then write the record and clear it
		if ((appName != prevAppName) && (prevAppName != "")) {
		   scalarsHTML = scalarsHTML + prevAppName + " by " + app.author + ",  launches: " + app.launchCount + ", Date: " + String.format('%td %<tb %<tY', app.useDate) + "<br>"
		   app.save(flush:true)
		}
		locDate.time = 0 // long 0 is beginning of 1 Jan 1970 GMT
		app.useDate = locDate + scalar.daysSince1970
		app.author = tempKey[2]
		switch(tempKey[0]) {
		  case "appActivationCount":
		     app.activationCount = scalar.value
		  case "appLaunchCount":
		     app.launchCount = scalar.value
		  case "appActiveTime":
		     app.activeTime = scalar.value
		  case "appBackgroundActiveTime":
		     app.backgroundActiveTime = scalar.value
		  case "appBackupFileSize":
		     app.backupFileSize = scalar.value
		} // end switch
		prevAppName = appName
	     } // if ! 'com'
//	     scalarsHTML = scalarsHTML + " " + tempKey[0] + "<br>"
          }  // end for
          render scalarsHTML
	  session.clear()
       }
       // render(view:"list",model:[ IpadScalars: scalars ],ipadScalarsInstanceTotal:"44")
       // render 'test view'
    }
}
