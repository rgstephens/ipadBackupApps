package ipadbackupapps

class IpadScalars implements Serializable {
    String key
    Integer daysSince1970
    Integer value

    static mapping = {
       table "Scalars"
       id composite:['key','daysSince1970','value'],generator:"assigned"
       version false
       key column:'key'
       daysSince1970 column:'daysSince1970'
       value column:'value'
    }

    static constraints = {
    }
}
