package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.Location
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LocationRepo: CrudRepository<Location, Long> {
}