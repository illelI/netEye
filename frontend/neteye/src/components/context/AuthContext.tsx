import { ReactNode, createContext, useContext, useState } from "react";
import * as React from 'react';

interface User {
    email: string;
    id: string;
  }
  
  interface AuthContextProps {
    user: User | null;
    login: (userData: User) => void;
    logout: () => void;
  }

  interface AuthProviderProps {
    children: ReactNode;
  }
  

const AuthContext = createContext<AuthContextProps | undefined>(undefined);

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
  
    const login = (userData: User) => {
      setUser(userData);
    };
  
    const logout = () => {
      setUser(null);
    };
  
    return (
      <AuthContext.Provider value={{ user, login, logout }}>
        {children}
      </AuthContext.Provider>
    );
  };
  
  export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
      throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
  };