import React, { useState } from "react";
import HeroSection from "../components/Hero";
import Navbar from "../components/NavBar";

const Home = () => {
  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(!isOpen);
    console.log(isOpen);
  };

  return (
    <>
      <Navbar toggle={toggle} />
      <HeroSection />
    </>
  );
};

export default Home;
