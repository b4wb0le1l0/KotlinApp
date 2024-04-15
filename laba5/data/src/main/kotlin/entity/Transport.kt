package entity

enum class Transport {
    FEW,
    NONE,
    NORMAL,
    ENOUGH;

    companion object {

        fun getEnums(): String {
            var strEnums = ""
            for (transport in View.values()) {
                strEnums += transport.name + ", "
            }
            return strEnums.substring(0..strEnums.length - 2)
        }
    }
}