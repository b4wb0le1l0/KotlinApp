package entity

enum class Furnish {
    DESIGNER,
    NONE,
    FINE,
    BAD;

    companion object {

        fun getEnums(): String {
            var strEnums = ""
            for (furnish in View.values()) {
                strEnums += furnish.name + ", "
            }
            return strEnums.substring(0..strEnums.length - 2)
        }
    }
}