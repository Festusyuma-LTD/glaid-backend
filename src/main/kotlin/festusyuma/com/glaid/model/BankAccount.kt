package festusyuma.com.glaid.model

import javax.persistence.Entity

@Entity
data class BankAccount (

        var bank: String,
        var accountNumber: String,
        var firstName: String,
        var lastName: String,
        var type: String
): Common()