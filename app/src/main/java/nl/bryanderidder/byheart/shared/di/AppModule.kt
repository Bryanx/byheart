package nl.bryanderidder.byheart.shared.di

import nl.bryanderidder.byheart.card.CardRepository
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.persistence.PileLocalRepository
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.SessionViewModel
import nl.bryanderidder.byheart.shared.database.CardDatabase
import nl.bryanderidder.byheart.store.StoreViewModel
import nl.bryanderidder.byheart.store.persistence.PileRemoteDao
import nl.bryanderidder.byheart.store.persistence.PileRemoteRepository
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

@JvmField
val appModule: Module = module {
    single { CardDatabase.getDatabase(get()) }
    single { get<CardDatabase>().cardDao() }
    single { get<CardDatabase>().pileLocalDao() }
    single { CardRepository(get()) }
    single { PileLocalRepository(get()) }
    single { PileRemoteDao() }
    single { PileRemoteRepository(get()) }

    viewModel { SessionViewModel(get()) }
    viewModel { CardViewModel(get(), get()) }
    viewModel { PileViewModel(get(), get()) }
    viewModel { StoreViewModel(get(), get()) }
}