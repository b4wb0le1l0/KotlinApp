package entity

enum class View {
    STREET,
    YARD,
    NORMAL,
    TERRIBLE;

    companion object {

        fun getEnums(): String {
            var strEnums = ""
            for (view in values()) {
                strEnums += view.name + ", "
            }
            return strEnums.substring(0..strEnums.length - 2)
        }
    }
}
