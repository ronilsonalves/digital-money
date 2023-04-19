import { NextResponse, type NextRequest } from 'next/server';
import jwtDecode from 'jwt-decode';

export function middleware(req: NextRequest) {
  const token = req.cookies.get('nextauth.token')?.value;

  if (!token) {
    return NextResponse.redirect(new URL('/', req.nextUrl.origin));
  }

  const decode = jwtDecode<{ exp: number }>(token);

  if (decode.exp * 1000 < Date.now()) {
    return NextResponse.redirect(new URL('/', req.nextUrl.origin));
  }

  return NextResponse.next();
}

export const config = {
  matcher: [
    '/dashboard/:path*',
    '/activities/:path*',
    '/profile/:path*',
    '/attach-money/:path*',
    '/send/:path*',
    '/cards/:path*',
  ],
};
