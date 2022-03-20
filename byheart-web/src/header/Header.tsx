import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import NightsStay from "@mui/icons-material/NightsStay";
import Settings from "@mui/icons-material/Settings";
import { useEffect } from "react";

export default function Header(props: { title?: string, hasBackButton?: boolean }) {
  const toggleDarkMode = () => {
    if (localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches))
      document.documentElement.classList.add('dark');
    else
      document.documentElement.classList.remove('dark');
  }

  const onClickDarkMode = () => {
    const isDarkTheme = localStorage.theme === 'dark';
    localStorage.theme = isDarkTheme ? 'light' : 'dark';
    toggleDarkMode();
  }

  // const login = () => {
  //   const provider = new GoogleAuthProvider();
  //   signInWithPopup(getAuth(), provider)
  //     .then((result) => {
  //       // This gives you a Google Access Token. You can use it to access the Google API.
  //       const credential = GoogleAuthProvider.credentialFromResult(result);
  //       const token = credential?.accessToken;
  //       // The signed-in user info.
  //       const user = result.user;
  //       // ...
  //     }).catch((error) => {
  //       // Handle Errors here.
  //       const errorCode = error.code;
  //       const errorMessage = error.message;
  //       console.log(errorMessage);

  //       // The email of the user's account used.
  //       const email = error.email;
  //       // The AuthCredential type that was used.
  //       const credential = GoogleAuthProvider.credentialFromError(error);
  //       // ...
  //     });
  // }

  useEffect(toggleDarkMode, []);
  return (
    <header className="text-gray-500 dark:text-gray-200 font-medium flex justify-between">
      {props.hasBackButton &&
        <button className="cursor-pointer p-5 filter active:brightness-75" onClick={() => window.history.back()} >
          <FontAwesomeIcon icon={faArrowLeft} />
        </button>}
      <div className="p-5">
        {props.title}
      </div>
      <section className="right-menu">
        <button className="cursor-pointer pt-5 pr-5 filter active:brightness-75" onClick={onClickDarkMode} >
          <NightsStay className="text-gray-500 dark:text-gray-200" />
        </button>
        <button className="cursor-pointer pt-5 pr-5 filter active:brightness-75" >
          <Settings className="text-gray-500 dark:text-gray-200" />
        </button>
      </section>
    </header>
  )
}
