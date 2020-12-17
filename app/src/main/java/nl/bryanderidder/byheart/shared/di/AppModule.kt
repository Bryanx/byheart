package nl.bryanderidder.byheart.shared.di

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.persistence.CardRepository
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.card.persistence.CardRemoteDao
import nl.bryanderidder.byheart.card.persistence.CardRemoteRepository
import nl.bryanderidder.byheart.pile.persistence.PileLocalRepository
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.SessionViewModel
import nl.bryanderidder.byheart.shared.database.CardDatabase
import nl.bryanderidder.byheart.shared.firestore.FireStoreViewModel
import nl.bryanderidder.byheart.pile.persistence.PileRemoteDao
import nl.bryanderidder.byheart.pile.persistence.PileRemoteRepository
import nl.bryanderidder.byheart.auth.AuthViewModel
import org.koin.android.ext.koin.androidApplication
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

    single { FirebaseFirestore.getInstance() }
    single { FirebaseAuth.getInstance() }

    single { PileRemoteDao(get()) }
    single { PileRemoteRepository(get()) }
    single { CardRemoteDao(get()) }
    single { CardRemoteRepository(get()) }

    single { GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(androidApplication().getString(R.string.default_web_client_id))
        .requestEmail()
        .build() }

    viewModel { SessionViewModel(get()) }
    viewModel { CardViewModel(get(), get()) }
    viewModel { PileViewModel(get(), get()) }
    viewModel { FireStoreViewModel(get(), get(), get()) }
    viewModel { AuthViewModel(get(), get(), get()) }
}