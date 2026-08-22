import { NavLink, Outlet } from "react-router-dom";

const NAV_ITEMS = [
	{ to: "/", label: "AI 상담", end: true },
	{ to: "/explore", label: "상권 탐색기" },
	{ to: "/compare", label: "지역 비교" },
	{ to: "/favorites", label: "관심 지역" },
];

function scrollToFooter() {
	document.querySelector(".site-footer")?.scrollIntoView({ behavior: "smooth", block: "start" });
}

export default function Layout() {
	return (
		<div className="site">
			<header className="site-header">
				<div className="site-header-inner">
					<div className="site-logo">
						<span className="brand-mark" aria-hidden="true" />
						<span className="site-logo-text">
							대세상권
							<span className="site-logo-tagline">AI 창업 입지 분석</span>
						</span>
					</div>
					<nav className="site-nav">
						{NAV_ITEMS.map((item) => (
							<NavLink
								key={item.to}
								to={item.to}
								end={item.end}
								className={({ isActive }) => `site-nav-link${isActive ? " active" : ""}`}
							>
								{item.label}
							</NavLink>
						))}
					</nav>
				</div>
			</header>

			<div className="site-body">
				<Outlet />
			</div>

			<footer className="site-footer">
				<p>
					데이터 출처: <strong>공공데이터포털(data.go.kr)</strong> — 소상공인시장진흥공단 상권정보
				</p>
				<p>※ AI 분석 리포트는 참고용이며 실제 창업 결정의 근거로 단독 사용하지 마십시오.</p>
			</footer>

			<button type="button" className="help-fab" onClick={scrollToFooter} aria-label="데이터 출처 안내">
				?
			</button>
		</div>
	);
}
